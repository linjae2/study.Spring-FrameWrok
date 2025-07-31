package com.violetbeach.hibernateplayground.hibernate.inheritance;

import com.violetbeach.hibernateplayground.address.AddressRepository;
import com.violetbeach.hibernateplayground.address.PrivateAddress;
import com.violetbeach.hibernateplayground.address.PrivateAddressRepository;
import com.violetbeach.hibernateplayground.address.SharedAddress;
import com.violetbeach.hibernateplayground.support.context.JpaTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InheritanceTest extends JpaTestContext {
    @Autowired
    AddressRepository<SharedAddress> sharedAddressRepository;
    @Autowired
    AddressRepository<PrivateAddress> privateAddressRepository;
    @Autowired
    PrivateAddressRepository par;

    @Test
    @DisplayName("Repositoryd의 Generic에서 사용된 타입은 캐스팅만 해주고, 다른 타입도 조회한다.")
    void withGeneric_findAll_ItReturnAllType() {
        // given
        PrivateAddress privateAddress = new PrivateAddress();
        privateAddressRepository.save(privateAddress);

        SharedAddress sharedAddress = new SharedAddress();
        sharedAddressRepository.save(sharedAddress);

        // when
        List<SharedAddress> allAddresses = sharedAddressRepository.findAll();

        // then
        assertThat(allAddresses).hasSize(2);
    }

    @Test
    @DisplayName("특정 Type의 Repository는 해당 타입의 엔터티만 조회한다.")
    void withInterface_findAll_ItReturnOnlyGenericType() {
        // given
        PrivateAddress expected = new PrivateAddress();
        privateAddressRepository.save(expected);

        SharedAddress sharedAddress = new SharedAddress();
        sharedAddressRepository.save(sharedAddress);

        // when
        List<PrivateAddress> privateAddresses = par.findAll();

        // then
        assertThat(privateAddresses).hasSize(1);

        PrivateAddress privateAddress = privateAddresses.get(0);
        assertThat(privateAddress).isEqualTo(expected);
    }

}
