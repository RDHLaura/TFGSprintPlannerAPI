package com.tfg.sprintplannerapi.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tfg.sprintplannerapi.utils.ObjectMapper;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Date;


@Data
public abstract class BaseDTO <T>  implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDTO.class);
    private static final long serialVersionUID = 5957406640789310067L;
    @JsonIgnore
    private final ObjectMapper mapper;
    private final Class<T> entityType;
    private Long id;
    private Date createDate;

    /**
     * Default contructor
     */
    public BaseDTO(){
        final ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        entityType =
                (Class<T>) genericSuperclass
                        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length -1];
       mapper = ObjectMapper.getInstance();
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
        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }





}
