package com.violetbeach.hibernateplayground.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity(name = "Account")
@SQLRestriction("active = true")
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Client client;
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType type;
    private Boolean active = true;

    public Account(AccountType type) {
        this.type = type;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}