package lkd.namsic.cnkb.config.converter;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    @Nullable
    public String convertToDatabaseColumn(List<String> attribute) {
        return CollectionUtils.isEmpty(attribute) ? null : String.join(";", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return StringUtils.isBlank(dbData) ? new ArrayList<>() : Arrays.asList(dbData.split(";"));
    }
}
