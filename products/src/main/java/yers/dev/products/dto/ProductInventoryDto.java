package yers.dev.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yers.dev.products.model.Category;

@Data
@Schema(description = "DTO for creating a product and its inventory")
public class ProductInventoryDto {

    @Schema(description = "Name of the product", example = "iPhone 15")
    private String name;

    @Schema(description = "Detailed description of the product", example = "Latest model of iPhone with USB-C port")
    private String description;

    @Schema(description = "Price of the product", example = "1299.99")
    private double price;

    @Schema(description = "Stock quantity to be saved with product", example = "100")
    private long stock_quantity;

    @Schema(description = "Product category", example = "ELECTRONICS")
    private Category category;

    @Schema(description = "Quantity to be saved in the inventory", example = "100")
    private Long quantity;

    @Schema(description = "Warehouse location to be saved in the inventory", example = "st. Petersburg, Russia")
    private String warehouseLocation; // TODO: implement

    @Schema(description = "Show is the active product or not", example = "true")
    private boolean isActive;

}
