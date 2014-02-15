package be.bittich.dynaorm.repository;

import be.bittich.dynaorm.dialect.Dialect;
import be.bittich.dynaorm.entity.Entity;
import be.bittich.dynaorm.entity.PrimaryKey;
import be.bittich.dynaorm.exception.BeanNotFoundException;
import static be.bittich.dynaorm.ioc.BasicContainer.getContainer;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;
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
public class AbstractBaseDynaRepository<T extends Entity> implements DynaRepository {

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
            List<T> object = runner.query("SELECT * FROM " + TENTITY.getTableName(),
                    getListHandler());
            return object;
        } catch (SQLException e) {
            LOG.log(Level.INFO, "erreur : {0}", e.getMessage());
        }
        return null;
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

    @Override
    public T findById(PrimaryKey... pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object update(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object delete(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List findBy(String value, String columnName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
