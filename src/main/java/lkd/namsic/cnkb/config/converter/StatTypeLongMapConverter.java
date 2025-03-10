package lkd.namsic.cnkb.config.converter;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import lkd.namsic.cnkb.enums.StatType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@Converter
public class StatTypeLongMapConverter implements AttributeConverter<Map<StatType, Long>, String> {

    @Override
    @Nullable
    public String convertToDatabaseColumn(Map<StatType, Long> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        return attribute.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(";"));
    }

    @Override
    public Map<StatType, Long> convertToEntityAttribute(String dbData) {
        Map<StatType, Long> map = new EnumMap<>(StatType.class);
        if (!StringUtils.isBlank(dbData)) {
            Arrays.stream(dbData.split(";"))
                .map(entry -> entry.split("="))
                .forEach(entrySplit -> map.put(StatType.valueOf(entrySplit[0]), Long.parseLong(entrySplit[1])));
        }

        return map;
    }
}
