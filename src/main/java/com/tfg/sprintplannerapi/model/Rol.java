package com.tfg.sprintplannerapi.model;

import java.util.ArrayList;
import java.util.List;

public enum Rol {
    DIRECTOR, CREADOR, PARTICIPANTE;

    //Devuelve el rol del usuario en el proyecto
    public static List<Rol> getRolUser(User userLogged, Project project) {
        String emailLogged = userLogged.getEmail();
        String emailCreator = project.getCreatedBy();
        String emailDirector = project.getDirector().getEmail();
        List<Rol> rolUser = new ArrayList<>();

        if (project.getTeam().contains(userLogged)) {
            rolUser.add(Rol.PARTICIPANTE);
        }

        if ( emailLogged.equals(emailCreator)){
            rolUser.add(Rol.CREADOR);
        }
        if (emailLogged.equals(emailDirector)) {
            rolUser.add(Rol.DIRECTOR);
        }

        return rolUser;
    }
}
