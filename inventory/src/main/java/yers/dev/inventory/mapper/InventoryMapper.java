package yers.dev.inventory.mapper;

import org.mapstruct.Mapper;
import yers.dev.inventory.entity.dto.InventoryDto;
import yers.dev.inventory.entity.Inventory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(InventoryDto inventoryDto);
    InventoryDto toInventoryDto(Inventory inventory);
    List<InventoryDto> toInventoryDto(List<Inventory> inventory);
}
