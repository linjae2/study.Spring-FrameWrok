package com.ventulus95.softdelete.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEST_MEMYN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(converter = YesNoConverter.class, columnName = "DEL_YN")
@Getter
@ToString
public class MemberYN implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testNo;

    private String name;

    @Column(name = "DEL_YN", insertable = false, updatable = false)
    private String delYn;

    @OneToMany(mappedBy = "memberYN", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipAddress> addresses = new ArrayList<>();


    public void addAddress(MembershipAddress address) {
        this.addresses.add(address);
    }




    public MemberYN(String name) {
        this.name = name;
    }
}
