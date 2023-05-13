package com.tfg.sprintplannerapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Data

public abstract class BaseDTO <T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseDTO.class);
    @JsonIgnore
    private final ModelMapper mapper;

    private final Class<T> entityType;


    /**
     * Default contructor
     */
    public BaseDTO(){
        final ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        entityType =
                (Class<T>) genericSuperclass
                        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length -1];
       mapper = new ModelMapper();
    }

    public void loadFromDomain(final T  entity){
        try{
            mapper.map(entity, this);
        }catch (Exception e){
            LOG.error(String.format("BaseDTO: loadFromDomain, Mapping exception with type %s", entityType.getName()));
        }
    }


    public T obtainFromDomain() throws NoSuchMethodException {
        T entity = null;
        try{
            entity = entityType.getDeclaredConstructor().newInstance();
            mapper.map(this, entity);
        } catch (InvocationTargetException  |
                 InstantiationException     |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }





}
