package net.badata.protobuf.converter.domain;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.exception.MappingException;
import net.badata.protobuf.converter.inspection.DefaultValue;
import net.badata.protobuf.converter.inspection.NullValueInspector;
import net.badata.protobuf.converter.mapping.DefaultMapperImpl;
import net.badata.protobuf.converter.mapping.MappingResult;
import net.badata.protobuf.converter.proto.ConverterProto;
import net.badata.protobuf.converter.resolver.AnnotatedFieldResolverFactoryImpl;
import net.badata.protobuf.converter.resolver.DefaultFieldResolverImpl;
import net.badata.protobuf.converter.resolver.FieldResolver;
import net.badata.protobuf.converter.type.DateLongConverterImpl;
import net.badata.protobuf.converter.type.EnumStringConverter;
import net.badata.protobuf.converter.type.SetListConverterImpl;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class ConverterDomain {

    @Data
    @ProtoClass(ConverterProto.ConverterTest.class)
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
        private PrimitiveTest primitiveValue;

        @ProtoField
        private FieldConverterTest fieldConversionValue;

        @ProtoField
        private NullDefaultTest nullDefaultValue;

        @ProtoField(name = "stringListValue")
        private List<String> simpleListValue;

        @ProtoField
        private List<PrimitiveTest> complexListValue;

        @ProtoField(converter = SetListConverterImpl.class)
        private Set<PrimitiveTest> complexSetValue;

        @ProtoField
        private List<PrimitiveTest> complexNullableCollectionValue;

        @ProtoField
        private ByteString bytesValue;

        @ProtoField
        private Test recursiveValue;
    }

    @Data
    @ProtoClass(ConverterProto.PrimitiveTest.class)
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

    @Getter
    @ProtoClass(ConverterProto.FieldConverterTest.class)
    @Setter
    public static class FieldConverterTest {

        @ProtoField(converter = TestEnumConverter.class)
        private TestEnumConverter.TestEnum enumString;

        @ProtoField(converter = DateLongConverterImpl.class)
        private Date dateLong;

        @ProtoField(converter = SetListConverterImpl.class)
        private Set<String> stringSetValue;
    }

    public static class TestEnumConverter extends EnumStringConverter<TestEnumConverter.TestEnum> {

        public enum TestEnum {
            ONE,
            TWO,
            THREE,
        }
    }

    @Getter
    @ProtoClass(ConverterProto.NullDefaultTest.class)
    @Setter
    public static class NullDefaultTest {

        @ProtoField
        private String nullString;

        @ProtoField(nullValue = AlwaysNullInspector.class, defaultValue = StringDefaultValue.class)
        private String customInspectionString;

        @ProtoField
        private PrimitiveWrapperTest defaultPrimitives;
    }

    @Getter
    @ProtoClass(ConverterProto.PrimitiveTest.class)
    @Setter
    public static class PrimitiveWrapperTest {

        @ProtoField
        private Long longValue;

        @ProtoField
        private Integer intValue;

        @ProtoField
        private Float floatValue;

        @ProtoField
        private Double doubleValue;

        @ProtoField
        private Boolean booleanValue;
    }

    public static class AlwaysNullInspector implements NullValueInspector {

        @Override
        public boolean isNull(final Object value) {
            return true;
        }
    }

    public static class StringDefaultValue implements DefaultValue {

        @Override
        public Object generateValue(final Class<?> type) {
            return "Custom default";
        }
    }

    public static class FieldResolverFactoryImpl extends AnnotatedFieldResolverFactoryImpl {

        public static final String FIELD_INT_VALUE = "intValue";
        public static final String FIELD_LONG_VALUE = "longValue";

        @Override
        public FieldResolver createResolver(final Field field) {
            if (FIELD_INT_VALUE.equals(field.getName())) {
                return super.createResolver(field);
            }

            if (FIELD_LONG_VALUE.equals(field.getName())) {
                DefaultFieldResolverImpl fieldResolver = (DefaultFieldResolverImpl) super.createResolver(field);
                fieldResolver.setProtobufName("longValueChanged");

                return fieldResolver;
            }

            return new DefaultFieldResolverImpl(field);
        }
    }

    public static class MultiMappingMapperImpl extends DefaultMapperImpl {

        @Override
        public <T extends Message.Builder> MappingResult mapToProtobufField(
            final FieldResolver fieldResolver,
            final Object domain,
            final T protobufBuilder
        ) throws MappingException {
            if (
                FieldResolverFactoryImpl.FIELD_INT_VALUE.equals(fieldResolver.getDomainName()) ||
                FieldResolverFactoryImpl.FIELD_LONG_VALUE.equals(fieldResolver.getDomainName())
            ) {
                return super.mapToProtobufField(fieldResolver, domain, protobufBuilder);
            }

            return new MappingResult(MappingResult.Result.MAPPED, null, protobufBuilder);
        }

        @Override
        public <T extends Message> MappingResult mapToDomainField(
            final FieldResolver fieldResolver,
            final T protobuf,
            final Object domain
        ) throws MappingException {
            if (
                FieldResolverFactoryImpl.FIELD_INT_VALUE.equals(fieldResolver.getDomainName()) ||
                FieldResolverFactoryImpl.FIELD_LONG_VALUE.equals(fieldResolver.getDomainName())
            ) {
                return super.mapToDomainField(fieldResolver, protobuf, domain);
            }

            return new MappingResult(MappingResult.Result.MAPPED, null, domain);
        }
    }
}
