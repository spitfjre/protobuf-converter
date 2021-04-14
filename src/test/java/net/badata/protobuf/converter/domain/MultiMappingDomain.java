package net.badata.protobuf.converter.domain;

import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoClasses;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.exception.MappingException;
import net.badata.protobuf.converter.mapping.DefaultMapperImpl;
import net.badata.protobuf.converter.mapping.MappingResult;
import net.badata.protobuf.converter.proto.MultiMappingProto;
import net.badata.protobuf.converter.resolver.AnnotatedFieldResolverFactoryImpl;
import net.badata.protobuf.converter.resolver.DefaultFieldResolverImpl;
import net.badata.protobuf.converter.resolver.FieldResolver;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class MultiMappingDomain {

    @Getter
    @ProtoClass(MultiMappingProto.MultiMappingTest.class)
    @Setter
    public static class MultiMappingOwner {

        @ProtoField
        private MultiMappingChild multiMappingValue;

        @ProtoField
        private List<MultiMappingChild> multiMappingListValue;
    }

    @Getter
    @ProtoClasses(
        {
            @ProtoClass(MultiMappingProto.MultiMappingFirst.class),
            @ProtoClass(
                value = MultiMappingProto.MultiMappingSecond.class,
                mapper = MultiMappingMapperImpl.class,
                fieldFactory = FieldResolverFactoryImpl.class
            ),
        }
    )
    @Setter
    public static class MultiMappingChild {

        @ProtoField
        private int intValue;

        @ProtoField
        private long longValue;
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
