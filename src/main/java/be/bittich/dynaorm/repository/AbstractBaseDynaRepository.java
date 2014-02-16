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
import be.bittich.dynaorm.core.AnnotationProcessor;
import be.bittich.dynaorm.dialect.Dialect;
import be.bittich.dynaorm.entity.Entity;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import be.bittich.dynaorm.exception.RequestInvalidException;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author Nordine
 * @param <T>
 */
public class AbstractBaseDynaRepository<T extends Entity> implements DynaRepository<T> {

    private static final long serialVersionUID = 1L;
    protected static final Logger LOG = Logger.getLogger("AbstractBaseDAO");
    private final Class<T> clazz;
    protected QueryRunner runner;
    protected Dialect dialect;

    private T TENTITY;

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
        try {
            TENTITY = clazz.newInstance();
            runner = getContainer().inject("queryRunner");
            dialect = getContainer().inject("dialect");
        } catch (InstantiationException | IllegalAccessException | BeanNotFoundException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public List findAll() {

        try {
            String request = dialect.selectAll(TENTITY.getTableName());
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
        String req = dialect.selectAll(TENTITY.getTableName());
        boolean firstIteration = true;
        List<String> parameters= new LinkedList();
        for (Field field : fieldPrimary.keySet()) {
            try {
                String label = "".equals(fieldPrimary.get(field).label()) ? field.getName() : fieldPrimary.get(field).label();
                String value = AnnotationProcessor.getFieldValue(field.getName(), t);
                if (firstIteration) {
                    req = dialect.where(req);
                    firstIteration = false;
                } else {
                    req = dialect.andWhere(req);
                }
                req = dialect.equalTo(req, label);
                parameters.add(value);
            } catch (RequestInvalidException ex) {
                Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            T result = runner.query(req, getHandler(),parameters.toArray());
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(AbstractBaseDynaRepository.class.getName()).log(Level.SEVERE, ex.getSQLState(), ex);
        }
        return null;
    }

    @Override
    public T update(T t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T delete(T t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findBy(String value, String columnName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSetHandler<List<T>> getListHandler() {
        ResultSetHandler<List<T>> handler = new BeanListHandler(clazz);
        return handler;
    }

    @Override
    public ResultSetHandler<T> getHandler() {
        ResultSetHandler<T> handler = new BeanHandler(clazz);
        return handler;
    }

}
