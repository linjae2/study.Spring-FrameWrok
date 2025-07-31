package com.violetbeach.hibernateplayground.hibernate.account;

import com.violetbeach.hibernateplayground.account.Account;
import com.violetbeach.hibernateplayground.account.AccountType;
import com.violetbeach.hibernateplayground.account.Client;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SQLRestrictionTest extends JpaTestContext {

    @Test
    @DisplayName("Client는 각 타입의 Account를 필드로 따로 가진다.")
    void clientHaveEachTypeAccountFields() {
        // given
        Client client = new Client();
        client.addAccount(new Account(AccountType.CREDIT));
        client.addAccount(new Account(AccountType.DEBIT));
        em.persist(client);
        em.flush();
        em.clear();

        // when
        Client get = em.find(Client.class, client.getId());

        // then
        assertThat(get.getCreditAccounts()).hasSize(1);
        assertThat(get.getDebitAccounts()).hasSize(1);
    }

}
