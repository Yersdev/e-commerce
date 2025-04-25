package yers.dev.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long product_id;

    private String name;

    private String description;

    private double price;

    private long stock_quantity;

    @Enumerated(EnumType.STRING)
    private Category category;

}
