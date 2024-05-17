package core.orm;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

        T save(T entity);
        Optional <T> findById(ID id);
        List<T> findAll();
        boolean existsById(ID id);
        void deleteById(ID id);
        long count();
}
