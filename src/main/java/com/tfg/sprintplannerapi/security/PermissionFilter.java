package com.tfg.sprintplannerapi.security;

import com.tfg.sprintplannerapi.bo.UserBO;
import com.tfg.sprintplannerapi.model.Project;
import com.tfg.sprintplannerapi.model.User;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;

//TODO no sirve, se cogerá los permisos desde los bo - borrar

@Component
public class  PermissionFilter <T>{
    @Autowired private UserBO userBO;
    User userLogged;
    public Boolean hasPermission (T entity, String methodRequest){

        userLogged = userBO.findUserLogged();
        //get entity name
        String[] path = entity.getClass().getName().split("\\.");
        String entityName = path[path.length -1];
        //asigno rol al usuario logueado comprobando si es
        Boolean permission = false;
        //creo un switch para hacer la llamada a la función que comprueba si el usuario tiene permiso para esa entidad
        switch (entityName){
            case "Project":
                permission = hasPermissionProject((Project) entity, methodRequest);
                break;
            case "Sprint":
                break;
            case "Task":
                break;
            case "Participation":
                //implementar código
                break;
        }

        return permission;
    }

    private Boolean hasPermissionProject(Project entity, String methodRequest){
        if(methodRequest == "put"){
            if( userLogged.getEmail() == entity.getDirector().getEmail() ||
                    userLogged.getEmail() == entity.getCreatedBy()){
                return true;
            }
            return false;
        }else if(methodRequest == "delete"){
            if( userLogged.getEmail() == entity.getCreatedBy()){
                return true;
            }
            return false;
        }
        return true;
    }


}
