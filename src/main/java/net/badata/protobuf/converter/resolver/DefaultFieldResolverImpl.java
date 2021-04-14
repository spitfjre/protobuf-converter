package net.badata.protobuf.converter.resolver;

import java.lang.reflect.Field;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.inspection.DefaultNullValueInspectorImpl;
import net.badata.protobuf.converter.inspection.DefaultValue;
import net.badata.protobuf.converter.inspection.NullValueInspector;
import net.badata.protobuf.converter.inspection.SimpleDefaultValueImpl;
import net.badata.protobuf.converter.type.DefaultConverterImpl;
import net.badata.protobuf.converter.type.TypeConverter;

/**
 * Default implementation of {@link net.badata.protobuf.converter.resolver.FieldResolver FieldResolver}.
 *
 * @author jsjem
 * @author Roman Gushel
 */
@Getter
public class DefaultFieldResolverImpl implements FieldResolver {

    private final Field field;
    private final String domainName;

    @Setter
    private String protobufName;

    private final Class<?> domainType;

    @Setter
    private Class<?> protobufType;

    @Setter
    private TypeConverter<?, ?> typeConverter;

    @Setter
    private NullValueInspector nullValueInspector;

    @Setter
    private DefaultValue defaultValue;

    /**
     * Constructor.
     *
     * @param field Domain field.
     */
    public DefaultFieldResolverImpl(final Field field) {
        this.field = field;
        this.domainName = field.getName();
        this.protobufName = field.getName();
        this.domainType = field.getType();
        this.protobufType = field.getType();
        this.typeConverter = new DefaultConverterImpl();
        this.nullValueInspector = new DefaultNullValueInspectorImpl();
        this.defaultValue = new SimpleDefaultValueImpl();
    }
}
