package net.badata.protobuf.converter;

import static net.badata.protobuf.converter.mapping.MappingResult.Result;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import net.badata.protobuf.converter.domain.MappingDomain;
import net.badata.protobuf.converter.exception.MappingException;
import net.badata.protobuf.converter.mapping.DefaultMapperImpl;
import net.badata.protobuf.converter.mapping.MappingResult;
import net.badata.protobuf.converter.proto.MappingProto;
import net.badata.protobuf.converter.resolver.AnnotatedFieldResolverFactoryImpl;
import net.badata.protobuf.converter.resolver.FieldResolver;
import net.badata.protobuf.converter.resolver.FieldResolverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class DefaultMapperTest {

    private DefaultMapperImpl mapper;
    private FieldResolverFactory fieldResolverFactory;
    private MappingDomain.Test testDomain;
    private MappingDomain.InaccessibleTest inaccessibleTestDomain;
    private MappingDomain.PrimitiveTest primitiveTestDomain;
    private MappingProto.MappingTest testProtobuf;

    @BeforeEach
    public void setUp() {
        mapper = new DefaultMapperImpl();
        fieldResolverFactory = new AnnotatedFieldResolverFactoryImpl();
        createTestProtobuf();
        createTestDomain();
        createPrimitiveTestDomain();
        inaccessibleTestDomain = new MappingDomain.InaccessibleTest();
    }

    @AfterEach
    public void tearDown() {
        mapper = null;
        testProtobuf = null;
        testDomain = null;
        inaccessibleTestDomain = null;
        primitiveTestDomain = null;
    }

    private void createTestProtobuf() {
        testProtobuf =
            MappingProto.MappingTest
                .newBuilder()
                .setBooleanValue(true)
                .setFloatValue(0.1f)
                .setDoubleValue(0.5)
                .setIntValue(1)
                .setLongValue(2L)
                .setStringValue("3")
                .setNestedValue(MappingProto.NestedTest.newBuilder().setStringValue("4"))
                .addStringListValue("10")
                .addNestedListValue(MappingProto.NestedTest.newBuilder().setStringValue("20"))
                .build();
    }

    private void createTestDomain() {
        testDomain = new MappingDomain.Test();
        testDomain.setBoolValue(false);
        testDomain.setFloatValue(0.2f);
        testDomain.setDoubleValue(0.6);
        testDomain.setIntValue(101);
        testDomain.setLongValue(102L);
        testDomain.setStringValue("103");
        MappingDomain.NestedTest nested = new MappingDomain.NestedTest();
        nested.setStringValue("104");
        testDomain.setNestedValue(nested);
        testDomain.setSimpleListValue(Arrays.asList("110"));
        MappingDomain.NestedTest nestedList = new MappingDomain.NestedTest();
        nested.setStringValue("120");
        testDomain.setNestedListValue(Arrays.asList(nestedList));
    }

    private void createPrimitiveTestDomain() {
        primitiveTestDomain = new MappingDomain.PrimitiveTest();
        primitiveTestDomain.setBooleanValue(true);
        primitiveTestDomain.setFloatValue(0.3f);
        primitiveTestDomain.setDoubleValue(0.7);
        primitiveTestDomain.setIntValue(201);
        primitiveTestDomain.setLongValue(202L);
    }

    @Test
    public void testMapObjectToDomain() throws MappingException {
        MappingResult result = mapper.mapToDomainField(findDomainField("floatValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getFloatValue(), testDomain);

        result = mapper.mapToDomainField(findDomainField("doubleValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getDoubleValue(), testDomain);

        result = mapper.mapToDomainField(findDomainField("intValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getIntValue(), testDomain);

        result = mapper.mapToDomainField(findDomainField("longValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getLongValue(), testDomain);

        result = mapper.mapToDomainField(findDomainField("stringValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getStringValue(), testDomain);
    }

    private FieldResolver findDomainField(final String fieldName) {
        try {
            return fieldResolverFactory.createResolver(MappingDomain.Test.class.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No such field: " + fieldName, e);
        }
    }

    private void testMappingResult(
        final MappingResult result,
        final MappingResult.Result code,
        final Object value,
        final Object destination
    ) {
        assertEquals(code, result.getCode());
        assertEquals(value, result.getValue());
        assertEquals(destination, result.getDestination());
    }

    @Test
    public void testMapFieldWithDifferentNameToDomain() throws MappingException {
        MappingResult result = mapper.mapToDomainField(findDomainField("boolValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.MAPPED, testProtobuf.getBooleanValue(), testDomain);
        result = mapper.mapToDomainField(findDomainField("simpleListValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.COLLECTION_MAPPING, testProtobuf.getStringListValueList(), testDomain);
    }

    @Test
    public void testMapCollectionToDomain() throws MappingException {
        MappingResult result = mapper.mapToDomainField(findDomainField("simpleListValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.COLLECTION_MAPPING, testProtobuf.getStringListValueList(), testDomain);

        result = mapper.mapToDomainField(findDomainField("nestedListValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.COLLECTION_MAPPING, testProtobuf.getNestedListValueList(), testDomain);
    }

    @Test
    public void testMapNestedToDomain() throws MappingException {
        MappingResult result = mapper.mapToDomainField(findDomainField("nestedValue"), testProtobuf, testDomain);
        testMappingResult(result, Result.NESTED_MAPPING, testProtobuf.getNestedValue(), testDomain);
    }

    @Test
    public void testMapNestedToDomainNull() throws MappingException {
        final MappingProto.MappingTest testProtobuf = MappingProto.MappingTest.newBuilder().build();
        final MappingDomain.Test testDomain = MappingDomain.Test.builder().build();

        final MappingResult result = mapper.mapToDomainField(findDomainField("nestedValue"), testProtobuf, testDomain);
        assertThat(result.getValue()).isNull();
    }

    @Test
    public void testMapObjectToProtobuf() throws MappingException {
        MappingProto.MappingTest.Builder protobufBuilder = MappingProto.MappingTest.newBuilder();
        MappingResult result = mapper.mapToProtobufField(findDomainField("boolValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getBoolValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("floatValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getFloatValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("doubleValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getDoubleValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("intValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getIntValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("longValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getLongValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("stringValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, testDomain.getStringValue(), protobufBuilder);
    }

    @Test
    public void testMapCollectionToProtobuf() throws MappingException {
        MappingProto.MappingTest.Builder protobufBuilder = MappingProto.MappingTest.newBuilder();
        MappingResult result = mapper.mapToProtobufField(
            findDomainField("simpleListValue"),
            testDomain,
            protobufBuilder
        );
        testMappingResult(result, Result.COLLECTION_MAPPING, testDomain.getSimpleListValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findDomainField("nestedListValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.COLLECTION_MAPPING, testDomain.getNestedListValue(), protobufBuilder);
    }

    @Test
    public void testMapNestedToProtobuf() throws MappingException {
        MappingProto.MappingTest.Builder protobufBuilder = MappingProto.MappingTest.newBuilder();
        MappingResult result = mapper.mapToProtobufField(findDomainField("nestedValue"), testDomain, protobufBuilder);
        testMappingResult(result, Result.NESTED_MAPPING, testDomain.getNestedValue(), protobufBuilder);
    }

    @Test
    public void testMapPrimitiveToProtobuf() throws MappingException {
        MappingProto.MappingTest.Builder protobufBuilder = MappingProto.MappingTest.newBuilder();
        MappingResult result = mapper.mapToProtobufField(
            findPrimitiveField("booleanValue"),
            primitiveTestDomain,
            protobufBuilder
        );
        testMappingResult(result, Result.MAPPED, primitiveTestDomain.isBooleanValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findPrimitiveField("floatValue"), primitiveTestDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, primitiveTestDomain.getFloatValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findPrimitiveField("doubleValue"), primitiveTestDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, primitiveTestDomain.getDoubleValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findPrimitiveField("intValue"), primitiveTestDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, primitiveTestDomain.getIntValue(), protobufBuilder);

        result = mapper.mapToProtobufField(findPrimitiveField("longValue"), primitiveTestDomain, protobufBuilder);
        testMappingResult(result, Result.MAPPED, primitiveTestDomain.getLongValue(), protobufBuilder);
    }

    private FieldResolver findPrimitiveField(final String fieldName) {
        try {
            return fieldResolverFactory.createResolver(MappingDomain.PrimitiveTest.class.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No such field: " + fieldName, e);
        }
    }

    @Test
    public void testInaccessibleDomainField() {
        assertThrows(
            MappingException.class,
            () ->
                mapper.mapToProtobufField(
                    findInaccessibleField("inaccessibleField"),
                    inaccessibleTestDomain,
                    MappingProto.MappingTest.newBuilder()
                )
        );
    }

    private FieldResolver findInaccessibleField(final String fieldName) {
        try {
            return fieldResolverFactory.createResolver(
                MappingDomain.InaccessibleTest.class.getDeclaredField(fieldName)
            );
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No such field: " + fieldName, e);
        }
    }

    @Test
    public void testProtectedDomainGetter() {
        assertThrows(
            MappingException.class,
            () ->
                mapper.mapToProtobufField(
                    findInaccessibleField("protectedGetterField"),
                    inaccessibleTestDomain,
                    MappingProto.MappingTest.newBuilder()
                )
        );
    }
}
