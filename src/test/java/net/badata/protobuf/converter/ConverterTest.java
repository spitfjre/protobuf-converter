package net.badata.protobuf.converter;

import static net.badata.protobuf.converter.domain.ConverterDomain.TestEnumConverter.TestEnum;
import static org.junit.jupiter.api.Assertions.*;

import com.google.protobuf.ByteString;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import net.badata.protobuf.converter.domain.ConverterDomain;
import net.badata.protobuf.converter.proto.ConverterProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class ConverterTest {

    private ConverterDomain.Test testDomain;
    private ConverterProto.ConverterTest testProtobuf;

    private FieldsIgnore fieldsIgnore;

    @BeforeEach
    public void setUp() {
        createTestProtobuf();
        createTestDomain();
        createIgnoredFieldsMap();
    }

    private void createTestProtobuf() {
        testProtobuf =
            ConverterProto.ConverterTest
                .newBuilder()
                .setBooleanValue(false)
                .setFloatValue(0.1f)
                .setDoubleValue(0.5)
                .setIntValue(1)
                .setLongValue(2L)
                .setStringValue("3")
                .setPrimitiveValue(
                    ConverterProto.PrimitiveTest
                        .newBuilder()
                        .setBooleanValue(true)
                        .setFloatValue(-0.1f)
                        .setDoubleValue(-0.5)
                        .setIntValue(-1)
                        .setLongValue(-2L)
                )
                .setFieldConversionValue(
                    ConverterProto.FieldConverterTest
                        .newBuilder()
                        .setEnumString("THREE")
                        .setDateLong(System.currentTimeMillis())
                        .addStringSetValue("11")
                )
                .setNullDefaultValue(
                    ConverterProto.NullDefaultTest
                        .newBuilder()
                        .setCustomInspectionString("Assumed as null value")
                        .setDefaultPrimitives(ConverterProto.PrimitiveTest.newBuilder())
                )
                .addStringListValue("10")
                .addComplexListValue(ConverterProto.PrimitiveTest.newBuilder().setIntValue(1001))
                .addComplexSetValue(ConverterProto.PrimitiveTest.newBuilder().setIntValue(1002))
                .setBytesValue(ByteString.copyFrom(new byte[] { 0, 1, 3, 7 }))
                .setRecursiveValue(ConverterProto.ConverterTest.newBuilder().setIntValue(1))
                .build();
    }

    private void createTestDomain() {
        ConverterDomain.PrimitiveTest primitiveTest = new ConverterDomain.PrimitiveTest();
        primitiveTest.setBooleanValue(true);
        primitiveTest.setFloatValue(-0.2f);
        primitiveTest.setDoubleValue(-0.6);
        primitiveTest.setIntValue(-101);
        primitiveTest.setLongValue(-102L);

        ConverterDomain.FieldConverterTest fieldConverterTest = new ConverterDomain.FieldConverterTest();
        fieldConverterTest.setEnumString(TestEnum.TWO);
        fieldConverterTest.setDateLong(new Date());
        Set<String> stringSet = new HashSet<String>();
        stringSet.add("111");
        fieldConverterTest.setStringSetValue(stringSet);

        testDomain = new ConverterDomain.Test();
        testDomain.setBoolValue(false);
        testDomain.setFloatValue(0.2f);
        testDomain.setDoubleValue(0.6);
        testDomain.setIntValue(101);
        testDomain.setLongValue(102L);
        testDomain.setStringValue("103");
        testDomain.setPrimitiveValue(primitiveTest);
        testDomain.setFieldConversionValue(fieldConverterTest);
        testDomain.setSimpleListValue(Arrays.asList("110"));

        ConverterDomain.PrimitiveTest primitiveTestItem = new ConverterDomain.PrimitiveTest();
        primitiveTestItem.setIntValue(-1001);
        testDomain.setComplexListValue(Arrays.asList(primitiveTestItem));
        ConverterDomain.PrimitiveTest primitiveTestSItem = new ConverterDomain.PrimitiveTest();
        primitiveTestItem.setIntValue(-1002);
        testDomain.setComplexSetValue(new HashSet<>(Arrays.asList(primitiveTestSItem)));
        testDomain.setComplexNullableCollectionValue(null);

        testDomain.setBytesValue(ByteString.copyFrom(new byte[] { 0, 1, 3, 7 }));

        ConverterDomain.Test nestedValue = new ConverterDomain.Test();
        nestedValue.setIntValue(1);
        testDomain.setRecursiveValue(nestedValue);
    }

    private void createIgnoredFieldsMap() {
        fieldsIgnore = new FieldsIgnore();
        fieldsIgnore.add(ConverterDomain.PrimitiveTest.class);
        fieldsIgnore.add(ConverterDomain.FieldConverterTest.class, "enumString");
        fieldsIgnore.add(ConverterDomain.Test.class, "boolValue");
    }

    @Test
    public void testProtobufToDomain() {
        ConverterDomain.Test result = Converter.create().toDomain(ConverterDomain.Test.class, testProtobuf);

        assertNotNull(result);

        assertEquals(testProtobuf.getBooleanValue(), result.getBoolValue());
        assertEquals((Object) testProtobuf.getFloatValue(), result.getFloatValue());
        assertEquals((Object) testProtobuf.getDoubleValue(), result.getDoubleValue());
        assertEquals((Object) testProtobuf.getIntValue(), result.getIntValue());
        assertEquals((Object) testProtobuf.getLongValue(), result.getLongValue());
        assertEquals(testProtobuf.getStringValue(), result.getStringValue());

        ConverterProto.PrimitiveTest primitiveProto = testProtobuf.getPrimitiveValue();
        ConverterDomain.PrimitiveTest primitiveDomain = result.getPrimitiveValue();

        assertEquals(primitiveProto.getLongValue(), primitiveDomain.getLongValue());
        assertEquals(primitiveProto.getIntValue(), primitiveDomain.getIntValue());
        assertEquals(primitiveProto.getFloatValue(), primitiveDomain.getFloatValue(), 0f);
        assertEquals(primitiveProto.getDoubleValue(), primitiveDomain.getDoubleValue(), 0);
        assertEquals(primitiveProto.getBooleanValue(), primitiveDomain.isBooleanValue());

        ConverterProto.FieldConverterTest conversionProto = testProtobuf.getFieldConversionValue();
        ConverterDomain.FieldConverterTest conversionDomain = result.getFieldConversionValue();

        assertEquals(conversionProto.getDateLong(), conversionDomain.getDateLong().getTime());
        assertEquals(conversionProto.getEnumString(), conversionDomain.getEnumString().name());
        assertTrue(conversionDomain.getStringSetValue().remove(conversionProto.getStringSetValue(0)));

        ConverterDomain.NullDefaultTest nullDefaultDomain = result.getNullDefaultValue();

        assertEquals(
            nullDefaultDomain.getCustomInspectionString(),
            new ConverterDomain.StringDefaultValue().generateValue(null)
        );
        assertNull(nullDefaultDomain.getDefaultPrimitives().getLongValue());
        assertNull(nullDefaultDomain.getDefaultPrimitives().getIntValue());
        assertNull(nullDefaultDomain.getDefaultPrimitives().getFloatValue());
        assertNull(nullDefaultDomain.getDefaultPrimitives().getDoubleValue());

        assertEquals(testProtobuf.getStringListValue(0), result.getSimpleListValue().get(0));
        assertEquals(
            testProtobuf.getComplexListValue(0).getIntValue(),
            result.getComplexListValue().get(0).getIntValue()
        );
        assertEquals(
            testProtobuf.getComplexSetValue(0).getIntValue(),
            result.getComplexSetValue().iterator().next().getIntValue()
        );

        assertTrue(result.getComplexNullableCollectionValue().isEmpty());

        assertEquals(testProtobuf.getBytesValue(), result.getBytesValue());
        assertEquals((Object) testProtobuf.getRecursiveValue().getIntValue(), result.getRecursiveValue().getIntValue());
    }

    @Test
    public void testFieldIgnoreProtobufToDomain() {
        Configuration configuration = Configuration.builder().addIgnoredFields(fieldsIgnore).build();
        ConverterDomain.Test result = Converter
            .create(configuration)
            .toDomain(ConverterDomain.Test.class, testProtobuf);

        assertNotNull(result);

        assertNull(result.getBoolValue());
        assertNull(result.getPrimitiveValue());
        assertNull(result.getFieldConversionValue().getEnumString());
        assertNull(result.getComplexListValue());
    }

    @Test
    public void testDomainToProtobuf() {
        ConverterProto.ConverterTest result = Converter
            .create()
            .toProtobuf(ConverterProto.ConverterTest.class, testDomain);

        assertNotNull(result);

        assertEquals(testDomain.getBoolValue(), result.getBooleanValue());
        assertEquals(testDomain.getFloatValue(), (Object) result.getFloatValue());
        assertEquals(testDomain.getDoubleValue(), (Object) result.getDoubleValue());
        assertEquals(testDomain.getIntValue(), (Object) result.getIntValue());
        assertEquals(testDomain.getLongValue(), (Object) result.getLongValue());
        assertEquals(testDomain.getStringValue(), result.getStringValue());

        ConverterProto.PrimitiveTest primitiveProto = result.getPrimitiveValue();
        ConverterDomain.PrimitiveTest primitiveDomain = testDomain.getPrimitiveValue();

        assertEquals(primitiveDomain.getLongValue(), primitiveProto.getLongValue());
        assertEquals(primitiveDomain.getIntValue(), primitiveProto.getIntValue());
        assertEquals(primitiveDomain.getFloatValue(), primitiveProto.getFloatValue(), 0f);
        assertEquals(primitiveDomain.getDoubleValue(), primitiveProto.getDoubleValue(), 0);
        assertEquals(primitiveDomain.isBooleanValue(), primitiveProto.getBooleanValue());

        ConverterProto.FieldConverterTest conversionProto = result.getFieldConversionValue();
        ConverterDomain.FieldConverterTest conversionDomain = testDomain.getFieldConversionValue();

        assertEquals(conversionDomain.getDateLong().getTime(), conversionProto.getDateLong());
        assertEquals(conversionDomain.getEnumString().name(), conversionProto.getEnumString());
        assertTrue(conversionDomain.getStringSetValue().remove(conversionProto.getStringSetValue(0)));

        assertFalse(result.hasNullDefaultValue());

        assertEquals(testDomain.getSimpleListValue().get(0), result.getStringListValue(0));
        assertEquals(
            testDomain.getComplexListValue().get(0).getIntValue(),
            result.getComplexListValue(0).getIntValue()
        );
        assertEquals(
            testDomain.getComplexSetValue().iterator().next().getIntValue(),
            result.getComplexSetValue(0).getIntValue()
        );

        assertTrue(result.getComplexNullableCollectionValueList().isEmpty());
        assertEquals((Object) testDomain.getRecursiveValue().getIntValue(), result.getRecursiveValue().getIntValue());
    }

    @Test
    public void testFieldIgnoreDomainToProtobuf() {
        Configuration configuration = Configuration.builder().addIgnoredFields(fieldsIgnore).build();
        ConverterProto.ConverterTest result = Converter
            .create(configuration)
            .toProtobuf(ConverterProto.ConverterTest.class, testDomain);

        assertNotNull(result);

        assertFalse(result.getBooleanValue());
        assertFalse(result.hasPrimitiveValue());
        assertEquals("", result.getFieldConversionValue().getEnumString());
        assertTrue(result.getComplexListValueList().isEmpty());
    }
}
