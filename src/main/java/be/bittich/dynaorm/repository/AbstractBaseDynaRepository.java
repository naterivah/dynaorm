/*
 * Copyright 2014 Nordine.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.bittich.dynaorm.repository;

import be.bittich.dynaorm.annotation.MetaColumn;
import be.bittich.dynaorm.annotation.PrimaryKey;
import be.bittich.dynaorm.annotation.TableFromDB;
import be.bittich.dynaorm.core.AnnotationProcessor;
import be.bittich.dynaorm.dialect.Dialect;
import static be.bittich.dynaorm.dialect.StringQueryBuilder.conditionPrimaryKeysBuilder;
import be.bittich.dynaorm.exception.ColumnNotFoundException;
import be.bittich.dynaorm.exception.EntityDoesNotExistException;
import be.bittich.dynaorm.exception.RequestInvalidException;
import be.bittich.dynaorm.ioc.BasicContainer;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import be.bittich.dynaorm.maping.ColumnMapping;
import be.bittich.dynaorm.maping.DynaRowProcessor;
import com.google.common.base.Joiner;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 *
 * @author Nordine
 * @param <T>
 */
public class AbstractBaseDynaRepository<T> implements DynaRepository<T> {

    private static final long serialVersionUID = 1L;
    protected static final Logger LOG = Logger.getLogger("AbstractBaseDAO");
    private final Class<T> clazz;
    protected QueryRunner runner;
    protected Dialect dialect;
    protected TableColumn tableColumn;
    protected RowProcessor rowProcessor;

    private Class<T> getClazz() {
        Class<T> clazzz = (Class<T>) ((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0];

        return clazzz;
    }

    /**
     * Construct the repository
     *
     */
    protected AbstractBaseDynaRepository() {
        clazz = getClazz();
        configure();
    }

    @Override
    public TableColumn getTableColumn() {
        return tableColumn;
    }

    @Override
    public List findAll() {

        try {
            String request = dialect.selectAll(tableColumn.getTableName());
            List<T> results = runner.query(request,
                    getListHandler());
            return results;
        } catch (SQLException e) {
            LOG.log(Level.INFO, "erreur : {0}", e.getMessage());
        }
        return null;
    }

    @Override
    public T findById(T t) {
        Map<Field, PrimaryKey> fieldPrimary = AnnotationProcessor.getAnnotedFields(t, PrimaryKey.class);
        String req = dialect.selectAll(tableColumn.getTableName());
        try {
            KeyValue<String, List<String>> pkBuilt = conditionPrimaryKeysBuilder(t, fieldPrimary, dialect);
            req = req.concat(pkBuilt.getKey());
            T result = runner.query(req, getHandler(), pkBuilt.getValue().toArray());
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, ex.getSQLState(), ex);
        } catch (RequestInvalidException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Boolean delete(T t) throws EntityDoesNotExistException {
        try {
            T tFromDB = this.findById(t);
            if (tFromDB == null) {
                throw new EntityDoesNotExistException();
            }
            Map<Field, PrimaryKey> fieldPrimary = AnnotationProcessor.getAnnotedFields(t, PrimaryKey.class);
            String req = dialect.delete(tableColumn.getTableName());
            KeyValue<String, List<String>> pkBuilt = conditionPrimaryKeysBuilder(t, fieldPrimary, dialect);
            req = req.concat(pkBuilt.getKey());
            runner.update(req, pkBuilt.getValue().toArray());
            return true;
        } catch (RequestInvalidException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, ex.getSQLState(), ex);
        }
        return false;
    }

    @Override
    public List<T> findBy(String columnName, String value) throws ColumnNotFoundException, RequestInvalidException {
        Integer type = tableColumn.getColumns().get(columnName);
        if (type == null) {
            throw new ColumnNotFoundException(String.format("Column name %s does'nt exist on the table %s", columnName, tableColumn.getTableName()));
        }
        String req = dialect.selectAll(tableColumn.getTableName());
        req = dialect.where(req);
        req = dialect.equalTo(req, columnName);
        List<T> results = null;
        try {
            results = runner.query(req,
                    getListHandler(), value);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public void update(T t) {
        //check if exists, if yes update, if not insert
        T tFromDB = this.findById(t);

        if (tFromDB == null) {
            insert(t);
        } else {
            //update
        }
    }
    /**
     * TODO refactor this portion of code
     * @param t 
     */
    protected void insert(T t) {
        try {
            ColumnMapping mappingUtil = BasicContainer.getContainer().injectSafely("columnMapping");
            Map<String, Field> mapFields = mappingUtil.mapToSQLColumns(t, tableColumn);
            String format = "'%s'";
            List<String> columnNames = new ArrayList();
            List<String> values = new ArrayList();

            for (String columnName : mapFields.keySet()) {
                try {
                    Field field = mapFields.get(columnName);
                    field.setAccessible(true);
                    PrimaryKey annotationPK = field.getAnnotation(PrimaryKey.class);
                    //we don't add generated values to the request
                    if (annotationPK == null || !annotationPK.autoGenerated()) {
                        String val = String.format(format, field.get(t).toString());
                       
                        MetaColumn metaColumn = field.getAnnotation(MetaColumn.class);
                        if (metaColumn != null) {
                            if (metaColumn.notNull() && field.get(t) == null) {
                                throw new NullPointerException(String.format("Column %s should not be null!", columnName));
                            }
                        }
                        values.add(val);
                        columnNames.add(columnName);
                    }

                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            String columns = Joiner.on(',').join(columnNames);
            String vals = Joiner.on(",").join(values);
            String request = dialect.insert(tableColumn.getTableName(), columns, vals);
            runner.update(request);
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected ResultSetHandler<List<T>> getListHandler() {
        ResultSetHandler<List<T>> handler = new BeanListHandler(clazz, rowProcessor);
        return handler;
    }

    protected ResultSetHandler<T> getHandler() {
        ResultSetHandler<T> handler = new BeanHandler(clazz, rowProcessor);
        return handler;
    }

    private void configure() {
        runner = getContainer().injectSafely("queryRunner");
        dialect = getContainer().injectSafely("dialect");
        // default tableName
        TableFromDB table = AnnotationProcessor.getAnnotationType(clazz, TableFromDB.class);
        String tableName = table != null && !isEmpty(table.tableName()) ? table.tableName() : table.tableName();
        tableColumn = new TableColumn(tableName);
        rowProcessor = new DynaRowProcessor(tableColumn);
        try {
            ResultSet rs = runner.getDataSource().getConnection().prepareStatement(dialect.requestForTableColumns(tableName)).executeQuery();
            Integer nbColumns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= nbColumns; i++) {
                String name = rs.getMetaData().getColumnName(i);
                int type = rs.getMetaData().getColumnType(i);
                tableColumn.addColumn(name, type);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
