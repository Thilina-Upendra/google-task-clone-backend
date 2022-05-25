package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.SuperEntity;
import lk.ijse.dep8.tasks.entity.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface SuperDAO<T extends SuperEntity, ID extends Serializable> {
    T save(T entity);

    boolean existsById(ID pk);

    void deleteById(ID pk) ;

    Optional<T> findById(ID pk) ;

    List<T> findAll() ;

    long count() ;
}
