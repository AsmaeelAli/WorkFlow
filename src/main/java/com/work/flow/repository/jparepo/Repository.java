package com.work.flow.repository.jparepo;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void deleteById(ID id);

    void saveAll(List<T> entities);
}
