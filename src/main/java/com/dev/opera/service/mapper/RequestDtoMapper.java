package com.dev.opera.service.mapper;

public interface RequestDtoMapper<D, T> {
    T mapToModel(D dto);
}
