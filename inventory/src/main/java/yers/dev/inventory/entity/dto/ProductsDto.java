package yers.dev.inventory.entity.dto;

import lombok.Data;
import yers.dev.inventory.entity.Category;

@Data
public class ProductsDto {

    private long product_id;

    private String name;

    private String description;

    private double price;

    private long stock_quantity;

    private Category category;

}
