package com.tfg.sprintplannerapi.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")

public class User extends Audit{
    private static final long serialVersionUID = -5072505803681612517L;

    @Column(name = "email", nullable = false, length = 100)
    @NotNull
    private String email;

    @Column(name = "username", nullable = false, length = 50)
    @NotNull
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    @NotNull
    private String password;

    @Column(name = "avatar", length = 200)
    private String avatar = null;

}
