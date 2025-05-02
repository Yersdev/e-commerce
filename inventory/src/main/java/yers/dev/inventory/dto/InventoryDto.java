package yers.dev.inventory.dto;

import lombok.Data;

@Data
public class InventoryDto {

    private Long inventory_id;

    private Long quantity;

    private String warehouseLocation;

}
