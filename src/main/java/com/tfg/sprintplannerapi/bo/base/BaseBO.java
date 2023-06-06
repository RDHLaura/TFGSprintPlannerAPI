package com.tfg.sprintplannerapi.bo.base;

import com.tfg.sprintplannerapi.dto.BaseDTO;
import com.tfg.sprintplannerapi.error.MappingException;
import com.tfg.sprintplannerapi.error.NoContentException;
import com.tfg.sprintplannerapi.error.NotFoundException;
import com.tfg.sprintplannerapi.utils.ListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
                        .getActualTypeArguments()[genericSuperclass.getActualTypeArguments().length -2];
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

    @Transactional
    public Optional<T> findById(ID id){
        return repositorio.findById(id);
    }


    public Page<D> findAll(Pageable pageable) {
        Page<T> listEntity = repositorio.findAll(pageable);
        if(listEntity.getContent().isEmpty())
            throw new NoContentException();

        List<D> listDTOs = new ArrayList<>();
        try {
            listDTOs = ListMapper.map(listEntity.getContent(), dtoType);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new MappingException();
        }
        return new PageImpl<>(listDTOs, pageable, listEntity.getTotalElements());
    }

    public List<D> findAllList() {
        List<T> listEntity = repositorio.findAll();
        if(listEntity.isEmpty())
            throw new NoContentException();

        List<D> listDTOs = new ArrayList<>();
        try {
            listDTOs = ListMapper.map(listEntity, dtoType);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new MappingException();
        }
        return listDTOs;
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
