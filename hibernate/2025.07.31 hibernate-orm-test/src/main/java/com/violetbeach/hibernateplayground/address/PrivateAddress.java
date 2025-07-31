package com.violetbeach.hibernateplayground.address;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("private")
public class PrivateAddress extends Address {
}
