package lkd.namsic.cnkb.config.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lkd.namsic.cnkb.dto.Location;

import java.util.Arrays;
import java.util.List;

@Converter
public class LocationConverter implements AttributeConverter<Location, String> {
    @Override
    public String convertToDatabaseColumn(Location location) {
        return location.x() + ":" + location.y();
    }

    @Override
    public Location convertToEntityAttribute(String s) {
        List<Integer> coordinates = Arrays.stream(s.split(":")).map(Integer::parseInt).toList();

        return new Location(coordinates.getFirst(), coordinates.getLast());
    }
}
