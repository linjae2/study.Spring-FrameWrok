package com.violetbeach.hibernateplayground.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "CLIENT_TABLE")
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @SQLRestriction("account_type = 'DEBIT'")
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> debitAccounts = new ArrayList<>();

    @SQLRestriction("account_type = 'CREDIT'")
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> creditAccounts = new ArrayList<>();

    public void addAccount(Account account) {
        if(account.getType() == AccountType.CREDIT) {
            creditAccounts.add(account);
        } else {
            debitAccounts.add(account);
        }
        account.setClient(this);
    }
}