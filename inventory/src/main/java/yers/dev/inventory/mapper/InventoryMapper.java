package yers.dev.inventory.mapper;

import org.mapstruct.Mapper;
import yers.dev.inventory.dto.InventoryDto;
import yers.dev.inventory.entity.Inventory;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryDto inventoryDto);
    InventoryDto toInventoryDto(Inventory inventory);
}
