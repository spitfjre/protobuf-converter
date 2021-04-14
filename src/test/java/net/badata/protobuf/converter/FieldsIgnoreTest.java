package net.badata.protobuf.converter;

import static net.badata.protobuf.converter.domain.IgnoreDomain.IgnoreDataTest;
import static net.badata.protobuf.converter.domain.IgnoreDomain.NoIgnoreDataTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.utils.FieldUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class FieldsIgnoreTest {

    private FieldsIgnore fieldsIgnore;

    @BeforeEach
    public void setUp() {
        fieldsIgnore = new FieldsIgnore();
    }

    @AfterEach
    public void tearDown() {
        fieldsIgnore.clear();
    }

    @Test
    public void testFieldIgnore() throws NoSuchFieldException {
        fieldsIgnore.clear();

        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("protoFieldName")));

        fieldsIgnore.add(IgnoreDataTest.class, "fieldName", "protofield", "notProtoField");

        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("protoFieldName")));
        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("notProtoField")));

        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("notIgnored")));
        assertFalse(fieldsIgnore.ignored(NoIgnoreDataTest.class.getField("fieldName")));

        fieldsIgnore.remove(IgnoreDataTest.class, "fieldName", "protofield", "notProtoField");

        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("protoFieldName")));

        fieldsIgnore.add(IgnoreDataTest.class, (String) null);

        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));

        fieldsIgnore.add(IgnoreDataTest.class, (String[]) null);

        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
    }

    @Test
    public void testClassIgnore() {
        fieldsIgnore.clear();
        fieldsIgnore.add(IgnoreDataTest.class);
        for (Field field : IgnoreDataTest.class.getDeclaredFields()) {
            assertTrue(fieldsIgnore.ignored(field));
        }
        for (Field field : NoIgnoreDataTest.class.getDeclaredFields()) {
            Class<?> verifiedClass = FieldUtils.isCollectionType(field)
                ? FieldUtils.extractCollectionType(field)
                : field.getType();
            if (field.isAnnotationPresent(ProtoField.class) && !verifiedClass.equals(IgnoreDataTest.class)) {
                assertFalse(fieldsIgnore.ignored(field));
            } else {
                assertTrue(fieldsIgnore.ignored(field));
            }
        }
    }

    @Test
    public void testFieldThenClassIgnore() throws NoSuchFieldException {
        fieldsIgnore.clear();

        fieldsIgnore.add(IgnoreDataTest.class, "fieldName");

        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
        assertFalse(fieldsIgnore.ignored(IgnoreDataTest.class.getField("protoFieldName")));

        fieldsIgnore.add(IgnoreDataTest.class);

        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("fieldName")));
        assertTrue(fieldsIgnore.ignored(IgnoreDataTest.class.getField("protoFieldName")));
    }
}
