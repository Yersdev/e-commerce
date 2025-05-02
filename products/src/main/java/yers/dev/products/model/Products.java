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

    //@Column(unique = true, nullable = false, length = 50)
    private String name;

    //@Column(unique = true, nullable = false, length = 255)
    private String description;

    //@Column(nullable = false)
    private double price;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private Category category;

}