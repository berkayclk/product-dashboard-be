package com.paydaybank.dashboard.service;

import java.util.List;
import java.util.Optional;

/**
 * Generic service interface
 * @param <T> Model Type
 * @param <U> ID Field Type
 */
public interface BaseService <T, U> {

    Optional<T> findById(U id);

    List<T> finAll();

    Optional<T> create( Optional<T> product );

    Optional<T> update( Optional<T> product );

    Boolean deleteById( U id );

    Boolean delete( Optional<T> product );
}
