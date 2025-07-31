package com.violetbeach.hibernateplayground.compositepk;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "TABLE_SYSTEM_USER")
@IdClass(PK.class)
@NoArgsConstructor
public class SystemUser {
    @Id
    @GeneratedValue
    private Integer registrationId;
    @Id
    private String subsystem;
    @Id
    private String username;
    private String name;

    public PK getId() {
        return new PK(
                subsystem,
                username,
                registrationId
        );
    }

    public SystemUser(String subsystem, String username, String name) {
        this.subsystem = subsystem;
        this.username = username;
        this.name = name;
    }
}