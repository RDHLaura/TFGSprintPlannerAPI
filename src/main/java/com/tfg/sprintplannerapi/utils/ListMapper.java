package com.tfg.sprintplannerapi.utils;

import com.tfg.sprintplannerapi.dto.BaseDTO;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ListMapper {
    public static <T, D extends BaseDTO> List<D> map(final List<T> inputEntity, final Class<D> outputDTOClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final List<D> result = new ArrayList<>();
        for (T t : inputEntity) {
            final D dto = outputDTOClass.getDeclaredConstructor().newInstance();
            dto.loadFromDomain(t);
            result.add(dto);
        }
        return result;
    }
}
