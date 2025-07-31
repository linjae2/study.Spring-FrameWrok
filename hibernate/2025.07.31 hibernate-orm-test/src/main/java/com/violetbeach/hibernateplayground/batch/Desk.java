package com.violetbeach.hibernateplayground.batch;

import com.violetbeach.hibernateplayground.product.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@Table(name = "DESK_TABLE")
@AllArgsConstructor
@NoArgsConstructor
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Pen> pens = new ArrayList<>();

    public Desk(List<Pen> pens) {
        this.pens = pens;
    }

    public void setPens(List<Pen> pens) {
        this.pens = pens;
    }
}
