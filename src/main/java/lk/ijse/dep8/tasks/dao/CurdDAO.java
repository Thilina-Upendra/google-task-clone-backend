package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.SuperEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CurdDAO<T extends SuperEntity, ID extends Serializable> extends SuperDAO{
    T save(T entity);

    boolean existsById(ID pk);

    void deleteById(ID pk) ;

    Optional<T> findById(ID pk) ;

    List<T> findAll() ;

    long count() ;
}
