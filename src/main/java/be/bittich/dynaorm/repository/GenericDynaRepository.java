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

import be.bittich.dynaorm.core.TableColumn;
import be.bittich.dynaorm.annotation.PrimaryKey;
import be.bittich.dynaorm.annotation.TableFromDB;
import be.bittich.dynaorm.core.AnnotationProcessor;
import static be.bittich.dynaorm.core.AnnotationProcessor.getAnnotedFields;
import be.bittich.dynaorm.dialect.Dialect;
import static be.bittich.dynaorm.core.StringQueryBuilder.conditionPrimaryKeysBuilder;
import be.bittich.dynaorm.exception.ColumnNotFoundException;
import be.bittich.dynaorm.exception.EntityDoesNotExistException;
import be.bittich.dynaorm.exception.RequestInvalidException;
import static be.bittich.dynaorm.facad.IOCFacadGet.getColumnMapping;
import static be.bittich.dynaorm.facad.IOCFacadGet.getDialect;
import static be.bittich.dynaorm.facad.IOCFacadGet.getQueryRunner;
import be.bittich.dynaorm.maping.ColumnMapping;
import be.bittich.dynaorm.maping.DynaRowProcessor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
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
public abstract class GenericDynaRepository<T> implements DynaRepository<T> {

    private static final long serialVersionUID = 1L;
    protected static final Logger LOG = Logger.getLogger("GenericDynaRepository");
    private final Class<T> clazz;
    protected QueryRunner runner;
    protected Dialect dialect;
    protected TableColumn tableColumn;
    protected RowProcessor rowProcessor;

    public Class<T> getClazz() {
        Class<T> clazzz = (Class<T>) ((ParameterizedType) (getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0];

        return clazzz;
    }

    /**
     * Construct the repository
     *
     */
    protected GenericDynaRepository() {
        clazz = getClazz();
        configure();
    }

    @Override
    public TableColumn getTableColumn() {
        return tableColumn;
    }

    @Override
    public List<T> findAll() {

        try {
            String request = dialect.selectAll(tableColumn.getTableName());
            List<T> results = runner.query(request, getListHandler());
            if (results != null) {
                for (T result : results) {
                    //wait a little bit before the next request
                    this.loadInvoker(result);

                }
            }
            return results;
        } catch (SQLException e) {
            LOG.log(Level.INFO, "erreur : {0}", e.getMessage());
        }
        return null;
    }

    @Override
    
    public T findById(T t) {
        //get the primary keys
        Map<Field, PrimaryKey> fieldPrimary = getAnnotedFields(t, PrimaryKey.class);
        String req = dialect.selectAll(tableColumn.getTableName());
        try {
            //construct the request with parameters as a list of string(key is the request)
            KeyValue<String, List<String>> pkBuilt = conditionPrimaryKeysBuilder(t, fieldPrimary, dialect);
            //select * from T where ..
            req = req.concat(pkBuilt.getKey());
            T result = runner.query(req, getHandler(), pkBuilt.getValue().toArray());
            //just before returning the result
            if (result != null) {
                this.loadInvoker(result);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(GenericDynaRepository.class.getName()).log(Level.SEVERE, ex.getSQLState(), ex);
        } catch (RequestInvalidException ex) {
            Logger.getLogger(GenericDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Id Not found");
    }

    @Override
    public Boolean delete(T t) throws EntityDoesNotExistException {
        try {
            T tFromDB = this.findById(t);
            if (tFromDB == null) {
                throw new EntityDoesNotExistException();
            }
            Map<Field, PrimaryKey> fieldPrimary = getAnnotedFields(t, PrimaryKey.class);
            String req = dialect.delete(tableColumn.getTableName());
            KeyValue<String, List<String>> pkBuilt = conditionPrimaryKeysBuilder(t, fieldPrimary, dialect);
            req = req.concat(pkBuilt.getKey());
            runner.update(req, pkBuilt.getValue().toArray());
            return true;
        } catch (RequestInvalidException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, ex.getSQLState(), ex);
        }
        return false;
    }

    @Override
    /**
     * will not call loadInvoker to prevent multiple requests !
     */
    public List<T> findBy(String columnName, String value) throws ColumnNotFoundException, RequestInvalidException {
        Integer type = tableColumn.getTypeOfColumn(columnName);
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
            LOG.log(Level.SEVERE, null, ex);
        }

        return results;
    }

    @Override
    public void update(T t) {
        //check if exists, if yes update, if not insert
        T tFromDB = this.findById(t);
        if (tFromDB == null) {
            persist(t);
        } else {
            merge(t);
        }
    }
    
    
    /**
    * Update an entity to the db
    * @param t 
    */
    protected void merge(T t) {
        try {
            Map<Field, PrimaryKey> fieldPrimary = getAnnotedFields(t, PrimaryKey.class);
            KeyValue<String, List<String>> pkBuilt = conditionPrimaryKeysBuilder(t, fieldPrimary, dialect);
            //where...
            String conditions = pkBuilt.getKey();
            ColumnMapping mappingUtil = getColumnMapping();
            KeyValue<List<String>, List<String>> colVal = mappingUtil.getColumnsValuesMap(t, tableColumn);
            String request = dialect.update(tableColumn.getTableName(), colVal.getKey(), colVal.getValue(), conditions);
            colVal.getValue().addAll(pkBuilt.getValue());
            List<String> filteredValues = applyFilterValue(colVal.getValue());
            runner.update(request, filteredValues.toArray());
        } catch (IllegalAccessException | SQLException | RequestInvalidException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Apply some filters for values. i.e in mysql you need to convert boolean
     * by 0 or 1
     *
     * @param list
     * @return
     */
    protected List<String> applyFilterValue(List<String> list) {
        List<String> filteredValues = new LinkedList<>();
        for (String val : list) {
            filteredValues.add(dialect.doFilterValue(val));
        }
        return filteredValues;
    }

    /**
     * Persist the given object in the db
     *
     * @param t
     */
    protected void persist(T t) {
        try {
            ColumnMapping mappingUtil = getColumnMapping();
            KeyValue<List<String>, List<String>> colVal = mappingUtil.getColumnsValuesMap(t, tableColumn);
            String request = dialect.insert(tableColumn.getTableName(), colVal.getKey(), colVal.getValue());
            List<String> filteredValues = applyFilterValue(colVal.getValue());
            runner.update(request, filteredValues.toArray());
        } catch (IllegalAccessException | SQLException ex) {

            LOG.log(Level.SEVERE, null, ex);
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

    /**
     * Configuration
     */
    private void configure() {
        runner = getQueryRunner();
        dialect = getDialect();
        // default tableName
        TableFromDB table = AnnotationProcessor.getAnnotationType(clazz, TableFromDB.class);
        String tableName = clazz.getSimpleName().toLowerCase();
        if (table != null && !isEmpty(table.tableName())) {
            tableName = table.tableName();
        }
        tableColumn = new TableColumn(tableName);
        rowProcessor = new DynaRowProcessor(tableColumn);
        try {
            ResultSet rs = runner.getDataSource().getConnection().prepareStatement(dialect.requestForTableColumns(tableName)).executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            Integer nbColumns = metaData.getColumnCount();
            for (int i = 1; i <= nbColumns; i++) {
                String name = metaData.getColumnName(i);
                int type = metaData.getColumnType(i);
                tableColumn.addColumn(name, type);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add a callback template method for findById and findAll Mainly used for
     * lazy loading relation. Just override it and it will works!
     *
     * @param t
     */
    protected void loadInvoker(T t) {
        //do nothing by default
    }

    /**
     * execute a query sql and returns a resultset
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    @Override
   
    public ResultSet executeQuerySQL(String sql,Object...params) throws SQLException {
        Connection conn=runner.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i+1, params[i]);
        }
        return stmt.executeQuery();
    }
    
    /**
     * Execute a query sql and return an int
     * @param sql
     * @param params
     * @return
     * @throws SQLException 
     */

    @Override
    public int executeQueryUpdateSQL(String sql,Object...params) throws SQLException{
        Connection conn=runner.getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i+1, params[i]);
        }
         return stmt.executeUpdate();
    }
    
    

}
