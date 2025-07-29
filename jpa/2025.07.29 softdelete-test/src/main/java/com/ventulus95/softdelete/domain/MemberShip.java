package com.ventulus95.softdelete.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "TEST_M")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "DEL_YN")
@Getter
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testNo;

    private String name;

    @Column(name = "DEL_YN", insertable = false, updatable = false)
    private boolean isDelete;

    public MemberShip(String name) {
        this.name = name;
    }
}
