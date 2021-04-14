package net.badata.protobuf.converter.domain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.proto.ResolverProto;
import net.badata.protobuf.converter.type.TypeConverter;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class ResolverDomain {

    @Getter
    @ProtoClass(ResolverProto.ResolverTest.class)
    @Setter
    public static class Test {

        @ProtoField(name = "stringListValue", converter = CommaDelimitedStringListConverter.class)
        private String commaDelimitedStringValue;

        @ProtoField(name = "delimitedStringValue", converter = ListCommaDelimitedStringConverter.class)
        private List<String> stringList;
    }

    public static class CommaDelimitedStringListConverter implements TypeConverter<String, List<String>> {

        @Override
        public String toDomainValue(Object instance) {
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) instance;
            return String.join(",", stringList);
        }

        @Override
        public List<String> toProtobufValue(Object instance) {
            String stringValue = (String) instance;
            String[] splitStrings = stringValue.split(",");
            List<String> stringList = new LinkedList<>();
            stringList.addAll(Arrays.asList(splitStrings));
            return stringList;
        }
    }

    public static class ListCommaDelimitedStringConverter implements TypeConverter<List<String>, String> {

        @Override
        public List<String> toDomainValue(Object instance) {
            String stringValue = (String) instance;
            String[] splitStrings = stringValue.split(",");
            List<String> stringList = new LinkedList<>();
            stringList.addAll(Arrays.asList(splitStrings));
            return stringList;
        }

        @Override
        public String toProtobufValue(Object instance) {
            @SuppressWarnings("unchecked")
            List<String> stringList = (List<String>) instance;
            return String.join(",", stringList);
        }
    }
}
