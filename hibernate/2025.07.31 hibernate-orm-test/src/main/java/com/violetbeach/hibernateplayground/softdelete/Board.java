package com.violetbeach.hibernateplayground.softdelete;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name = "BOARD_TABLE")
@NoArgsConstructor
@SoftDelete(columnName = "is_deleted")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
