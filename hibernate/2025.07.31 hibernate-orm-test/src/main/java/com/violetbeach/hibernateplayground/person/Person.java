package com.violetbeach.hibernateplayground.person;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "PERSON_TABLE")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "address_table",
            joinColumns = @JoinColumn(name= "person_id", referencedColumnName = "id")
    )
    private List<String> addresses = new ArrayList<>();

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
