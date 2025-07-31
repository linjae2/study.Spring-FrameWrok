package com.violetbeach.hibernateplayground.batch;

import com.violetbeach.hibernateplayground.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PEN_TABLE")
@AllArgsConstructor
@NoArgsConstructor
public class Pen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "desk_id")
    private Desk desk;

    public Pen(Desk desk) {
        this.desk = desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }
}
