package lkd.namsic.cnkb.config.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lkd.namsic.cnkb.enums.ItemType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public abstract class EnumListConverter<T extends Enum<?>> implements AttributeConverter<List<T>, String> {

    protected abstract T findEnum(String name);

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        return attribute.stream()
            .map(Enum::name)
            .collect(Collectors.joining(";"));
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return new ArrayList<>();
        }

        return Arrays.stream(dbData.split(";"))
            .map(this::findEnum)
            .toList();
    }

    public static class ItemTypeListConverter extends EnumListConverter<ItemType> {

        @Override
        protected ItemType findEnum(String name) {
            return ItemType.valueOf(name);
        }
    }
}
