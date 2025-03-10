package lkd.namsic.cnkb.config.converter;

import jakarta.annotation.Nullable;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lkd.namsic.cnkb.enums.domain.ItemType;
import lkd.namsic.cnkb.enums.domain.NpcType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@Converter
public abstract class EnumCollectionConverter<T extends Enum<?>, C extends Collection<T>> implements AttributeConverter<C, String> {

    protected abstract T findEnum(String name);
    protected abstract C getEmptyCollection();
    protected abstract C collect(Stream<T> stream);

    @Override
    @Nullable
    public String convertToDatabaseColumn(C attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        return attribute.stream()
            .map(Enum::name)
            .collect(Collectors.joining(";"));
    }

    @Override
    public C convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return this.getEmptyCollection();
        }

        Stream<T> stream = Arrays.stream(dbData.split(";"))
            .map(this::findEnum);

        return this.collect(stream);
    }

    private abstract static class EnumListConverter<U extends Enum<?>> extends EnumCollectionConverter<U, List<U>> {

        @Override
        protected List<U> getEmptyCollection() {
            return new ArrayList<>();
        }

        @Override
        protected List<U> collect(Stream<U> stream) {
            return stream.toList();
        }
    }

    private abstract static class EnumSetConverter<U extends Enum<?>> extends EnumCollectionConverter<U, Set<U>> {

        @Override
        protected Set<U> getEmptyCollection() {
            return new HashSet<>();
        }

        @Override
        protected Set<U> collect(Stream<U> stream) {
            return stream.collect(Collectors.toSet());
        }
    }

    public static class ItemTypeListConverter extends EnumListConverter<ItemType> {

        @Override
        protected ItemType findEnum(String name) {
            return ItemType.valueOf(name);
        }
    }

    public static class NpcTypeSetConverter extends EnumSetConverter<NpcType> {

        @Override
        protected NpcType findEnum(String name) {
            return NpcType.valueOf(name);
        }
    }
}
