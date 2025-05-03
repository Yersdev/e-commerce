package yers.dev.inventory.dto;

import lombok.Data;
import yers.dev.inventory.entity.Category;

@Data
public class ProductInventoryDto {

    private long product_inventory_id;

    private String name;

    private String description;

    private double price;

    private long stock_quantity;

    private Category category;


    private String warehouseLocation;

}
