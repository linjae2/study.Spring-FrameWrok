package com.violetbeach.hibernateplayground.mask;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "MASK_TABLE")
@Comment(on = "MASK_TABLE", value = "The MASK TABLE")
@NoArgsConstructor
public class Mask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
