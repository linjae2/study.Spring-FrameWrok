package com.violetbeach.hibernateplayground.address;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "ADDRESS_TABLE2")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "type")
public abstract class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
}
