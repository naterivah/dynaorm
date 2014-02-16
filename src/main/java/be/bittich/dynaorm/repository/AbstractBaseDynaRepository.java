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

import be.bittich.dynaorm.annotation.PrimaryKey;
import be.bittich.dynaorm.annotation.TableFromDB;
import be.bittich.dynaorm.core.AnnotationProcessor;
import be.bittich.dynaorm.dialect.Dialect;
import static be.bittich.dynaorm.dialect.StringQueryBuilder.conditionPrimaryKeysBuilder;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.EntityDoesNotExistException;
import be.bittich.dynaorm.exception.RequestInvalidException;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
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
    private final String tableName;

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
      
            TableFromDB table = AnnotationProcessor.getAnnotationType(clazz, TableFromDB.class);
            if (table != null && !isEmpty(table.tableName())) {
                tableName = table.tableName();
            } else {
                // default tableName 
                tableName = clazz.getSimpleName().toUpperCase();
            }
            runner = getContainer().injectSafely("queryRunner");
            dialect = getContainer().injectSafely("dialect");
        

    }

    @Override
    public List findAll() {

        try {
            String request = dialect.selectAll(getTableName());
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
        String req = dialect.selectAll(getTableName());
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
    public T update(T t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean delete(T t) throws EntityDoesNotExistException {
        try {
            T tFromDB = this.findById(t);
            if (tFromDB == null) {
                throw new EntityDoesNotExistException();
            }
            Map<Field, PrimaryKey> fieldPrimary = AnnotationProcessor.getAnnotedFields(t, PrimaryKey.class);
            String req = dialect.delete(getTableName());
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
    public List<T> findBy(String value, String columnName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected ResultSetHandler<List<T>> getListHandler() {
        ResultSetHandler<List<T>> handler = new BeanListHandler(clazz);
        return handler;
    }

    protected ResultSetHandler<T> getHandler() {
        ResultSetHandler<T> handler = new BeanHandler(clazz);
        return handler;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

}
