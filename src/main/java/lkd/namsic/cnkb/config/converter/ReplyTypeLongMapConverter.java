package lkd.namsic.cnkb.config.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lkd.namsic.cnkb.enums.ReplyType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class ReplyTypeLongMapConverter implements AttributeConverter<Map<ReplyType, Long>, String> {

    @Override
    public String convertToDatabaseColumn(Map<ReplyType, Long> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        return attribute.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(";"));
    }

    @Override
    public Map<ReplyType, Long> convertToEntityAttribute(String dbData) {
        Map<ReplyType, Long> map = new HashMap<>();
        if (!StringUtils.isBlank(dbData)) {
            Arrays.stream(dbData.split(";"))
                .map(entry -> entry.split("="))
                .forEach(entrySplit -> map.put(ReplyType.valueOf(entrySplit[0]), Long.parseLong(entrySplit[1])));
        }

        return map;
    }
}
