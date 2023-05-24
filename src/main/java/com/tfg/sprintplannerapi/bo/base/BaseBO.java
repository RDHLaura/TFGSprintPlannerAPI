package com.tfg.sprintplannerapi.bo.base;

import com.tfg.sprintplannerapi.dto.BaseDTO;
import com.tfg.sprintplannerapi.error.MappingException;
import com.tfg.sprintplannerapi.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class BaseBO <T, ID, D extends BaseDTO<T>, R extends JpaRepository<T, ID>> {

    @Autowired
    protected R repositorio;

    Class<D> dtoType;


    /**
     * Default contructor
     */
    public BaseBO(){
        final ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        dtoType =
                (Class<D>) genericSuperclass
                        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length -1];
    }


    public T save(T t) {
        return repositorio.save(t);
    }

    public D findOneDTO(ID id) {
        T t = repositorio.findById(id).orElseThrow(NotFoundException::new);
        D dto =  null;
        try{
            dto = dtoType.getDeclaredConstructor().newInstance();
            dto.loadFromDomain(t);
        } catch (InvocationTargetException  |
                 InstantiationException     |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new MappingException();
        }
        return dto;
    }

    public Optional<T> findById(ID id){
        return repositorio.findById(id);
    }

    public List<T> findAll() {
        return repositorio.findAll();
    }

    public T edit(T t) {
        return repositorio.save(t);
    }

    public void delete(T t) {
        repositorio.delete(t);
    }

    public void deleteById(ID id) {
        repositorio.deleteById(id);
    }
}
