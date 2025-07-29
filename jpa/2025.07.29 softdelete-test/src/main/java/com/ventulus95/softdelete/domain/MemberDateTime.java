package com.ventulus95.softdelete.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.type.YesNoConverter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TEST_MEM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(converter = DelDatetimeConverter.class, strategy = SoftDeleteType.DELETED)
@Getter
public class MemberDateTime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testNo;

    private String name;
//
//    @Column(name = "DEL_DTM", insertable = false, updatable = false)
//    private LocalDateTime delDateTime;



    public MemberDateTime(String name) {
        this.name = name;
    }
}
