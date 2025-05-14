package yers.dev.inventory.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import yers.dev.inventory.entity.Category;


/**
 * DTO для информации о продукте на складе
 */
@Data
@Schema(name = "ProductInventoryDto", description = "Информация о продукте на складе")
public class ProductInventoryDto {

    /**
     * Идентификатор записи о продукте на складе
     */
    @NotNull(message = "product_inventory_id не должен быть пустым")

    @Schema(
            description = "Time representing when the error happened"
    )
    private Long product_inventory_id;

    /**
     * Название продукта
     */
    @NotBlank(message = "name не должен быть пустым")
    @Size(max = 255, message = "name не должен превышать 255 символов")
    private String name;

    /**
     * Описание продукта
     */
    @NotBlank(message = "description не должен быть пустым")
    private String description;

    /**
     * Цена продукта (>= 0)
     */
    @DecimalMin(value = "0.0", inclusive = true, message = "price должен быть неотрицательным")
    private double price;

    /**
     * Количество на складе (>= 0)
     */
    @Min(value = 0, message = "stock_quantity должен быть неотрицательным")
    private long stock_quantity;

    /**
     * Категория продукта
     */
    @NotNull(message = "category не должен быть пустым")
    private Category category;

    /**
     * Местоположение склада
     */
    @NotBlank(message = "warehouseLocation не должен быть пустым")
    private String warehouseLocation;
}
