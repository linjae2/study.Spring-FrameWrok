package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Entity @Table(name="category")
public class Category {
	@Id @GeneratedValue(generator = "abcde")
	@Column(name="category_id")
	private int id;

	private String name;
}

