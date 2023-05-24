package com.tfg.sprintplannerapi.utils;

import lombok.Data;
import org.dozer.CustomFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
public class ObjectMapper implements Serializable {
    private static final long serialVersionUID = -5218249007177724799L;
    private static final ObjectMapper instance;
    private static Mapper mapper = new DozerBeanMapper();

    /* Inicializa la clase */
    static {
        instance = new ObjectMapper();
    }
    /* Constructor */
    public static  ObjectMapper getInstance() {
        return instance;
    }

    public void map(final Object source, final Object destination){
        try{
            mapper.map(source, destination);
        }catch (final MappingException e){
            throw new com.tfg.sprintplannerapi.error.MappingException();
        }
    }

    /**
     * Previene que mapper lance la excepción LazyInitializationException si un campo del recurso tiene FetchType.LAZY
     */
    private class HibernateInitializedFieldMapper implements CustomFieldMapper {
        @Override
        public boolean mapField(
                final Object source,
                final Object destination,
                final Object sourceFieldValue,
                final ClassMap classMap,
                final FieldMap fieldMapping ){
            return Hibernate.isInitialized(sourceFieldValue);
        }
    }
}
