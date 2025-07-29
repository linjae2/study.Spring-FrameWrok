package com.ventulus95.softdelete.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

@Entity
@Table(name = "TEST_MEM_ADDRESS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SoftDelete(strategy = SoftDeleteType.ACTIVE)
@ToString
public class MembershipAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressNo;

    private String addressName;

    private String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberYN memberYN;


    public MembershipAddress(String addressName, String addressDetail) {
        this.addressName = addressName;
        this.addressDetail = addressDetail;
    }
}
