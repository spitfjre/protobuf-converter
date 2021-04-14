package net.badata.protobuf.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import net.badata.protobuf.converter.domain.ResolverDomain;
import net.badata.protobuf.converter.proto.ResolverProto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class ResolverTest {

    private static final String DELIMITED_STRING = "one,two,three";
    private static final List<String> STRING_LIST = Arrays.asList("four", "five", "six");

    private ResolverDomain.Test testDomain;
    private ResolverProto.ResolverTest testProtobuf;

    @BeforeEach
    public void setUp() {
        createTestDomain();
        createTestProtobuf();
    }

    private void createTestDomain() {
        testDomain = new ResolverDomain.Test();
        testDomain.setCommaDelimitedStringValue(DELIMITED_STRING);
        testDomain.setStringList(STRING_LIST);
    }

    private void createTestProtobuf() {
        testProtobuf =
            ResolverProto.ResolverTest
                .newBuilder()
                .addAllStringListValue(STRING_LIST)
                .setDelimitedStringValue(DELIMITED_STRING)
                .build();
    }

    @Test
    public void testDomainToProtobuf() {
        ResolverProto.ResolverTest result = Converter.create().toProtobuf(ResolverProto.ResolverTest.class, testDomain);

        assertNotNull(result);
        assertEquals(testDomain.getCommaDelimitedStringValue(), String.join(",", result.getStringListValueList()));
        assertEquals(testDomain.getStringList(), Arrays.asList(result.getDelimitedStringValue().split(",")));
    }

    @Test
    public void testProtobufToDomain() {
        ResolverDomain.Test result = Converter.create().toDomain(ResolverDomain.Test.class, testProtobuf);

        assertNotNull(result);
        assertEquals(
            testProtobuf.getStringListValueList(),
            Arrays.asList(result.getCommaDelimitedStringValue().split(","))
        );
        assertEquals(testProtobuf.getDelimitedStringValue(), String.join(",", result.getStringList()));
    }
}
