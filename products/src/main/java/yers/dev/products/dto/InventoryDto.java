package yers.dev.products.dto;

import lombok.Data;

@Data
public class InventoryDto {

    private Long inventory_id;

    private Long quantity;

    private String warehouseLocation; // TODO: implement

}
