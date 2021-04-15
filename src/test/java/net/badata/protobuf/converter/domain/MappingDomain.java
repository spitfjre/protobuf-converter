package net.badata.protobuf.converter.domain;

import java.util.List;
import lombok.*;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.proto.MappingProto;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class MappingDomain {

    @AllArgsConstructor
    @Builder(toBuilder = true)
    @Getter
    @NoArgsConstructor
    @ProtoClass(MappingProto.MappingTest.class)
    @Setter
    public static class Test {

        @ProtoField
        private Long longValue;

        @ProtoField
        private Integer intValue;

        @ProtoField
        private Float floatValue;

        @ProtoField
        private Double doubleValue;

        @ProtoField(name = "booleanValue")
        private Boolean boolValue;

        @ProtoField
        private String stringValue;

        @ProtoField
        private NestedTest nestedValue;

        @ProtoField(name = "stringListValue")
        private List<String> simpleListValue;

        @ProtoField
        private List<NestedTest> nestedListValue;
    }

    @Getter
    @ProtoClass(MappingProto.NestedTest.class)
    @Setter
    public static class NestedTest {

        @ProtoField
        private String stringValue;
    }

    @Getter
    @ProtoClass(MappingProto.MappingTest.class)
    @Setter
    public static class PrimitiveTest {

        @ProtoField
        private long longValue;

        @ProtoField
        private int intValue;

        @ProtoField
        private float floatValue;

        @ProtoField
        private double doubleValue;

        @ProtoField
        private boolean booleanValue;
    }

    @ProtoClass(MappingProto.MappingTest.class)
    public static class InaccessibleTest {

        @ProtoField
        private Object inaccessibleField;

        @Getter
        @ProtoField
        private Object protectedGetterField;
    }
}
