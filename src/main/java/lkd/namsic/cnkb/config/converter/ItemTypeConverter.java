package lkd.namsic.cnkb.config.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lkd.namsic.cnkb.enums.ItemType;

@Converter
public class ItemTypeConverter implements AttributeConverter<ItemType, String> {

    @Override
    public String convertToDatabaseColumn(ItemType attribute) {
        return attribute.getValue();
    }

    @Override
    public ItemType convertToEntityAttribute(String dbData) {
        return ItemType.find(dbData);
    }
}
