package com.violetbeach.hibernateplayground.address;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("shared")
public class SharedAddress extends Address {
}
