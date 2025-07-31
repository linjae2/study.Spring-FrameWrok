package com.violetbeach.hibernateplayground.compositepk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PK implements Serializable {

    private String subsystem;

    private String username;

    private Integer registrationId;

    public PK(String subsystem, String username) {
        this.subsystem = subsystem;
        this.username = username;
    }
}