package lkd.namsic.cnkb.config.converter;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lkd.namsic.cnkb.dto.Location;
import org.apache.commons.lang3.StringUtils;

@Converter
public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    @Nullable
    public String convertToDatabaseColumn(Location location) {
        if (location == null) {
            return null;
        }

        return location.x() + ":" + location.y();
    }

    @Override
    @Nullable
    public Location convertToEntityAttribute(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        String[] coordinates = s.split(":");
        return new Location(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
    }
}
