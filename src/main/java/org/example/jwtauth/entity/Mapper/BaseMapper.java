package org.example.jwtauth.entity.Mapper;

public interface BaseMapper<T, S> {

    S map(T source);
}
