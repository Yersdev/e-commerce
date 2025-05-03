package yers.dev.products.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import yers.dev.products.model.Category;

@Data
public class ProductsDto {

    private long product_id;

    private String name;

    private String description;

    private double price;

    private Category category;

}
