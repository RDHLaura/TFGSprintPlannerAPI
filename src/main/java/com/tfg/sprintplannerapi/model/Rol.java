package com.tfg.sprintplannerapi.model;

import java.util.ArrayList;
import java.util.List;

public enum Rol {
    DIRECTOR, CREADOR, PARTICIPANTE;

    //Devuelve el rol del usuario en el proyecto
    public static List<Rol> getRolUser(User userLogged, Project project) {

        List<Rol> rolUser = new ArrayList<>();

        if (userLogged.getEmail() == project.getCreatedBy()){
            rolUser.add(Rol.CREADOR);
        }
        if (userLogged.getEmail() == project.getDirector().getEmail()) {
            rolUser.add(Rol.DIRECTOR);
        }
        if (project.getTeam().contains(userLogged)) {
            rolUser.add(Rol.PARTICIPANTE);
        }
        return rolUser;
    }
}
