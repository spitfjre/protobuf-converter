package net.badata.protobuf.converter.utils;

import com.google.protobuf.Message;
import java.lang.reflect.ParameterizedType;
import lombok.experimental.UtilityClass;

/**
 * Utilities for extract Protobuf messages .
 *
 * @author jsjem
 * @author Roman Gushel
 */
@UtilityClass
public class MessageUtils {

    /**
     * Extract Protobuf message type.
     *
     * @param object     Object that contains Protobuf message.
     * @param methodName getter method name.
     * @return Class of the Protobuf message.
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Message> getMessageType(final Object object, final String methodName) {
        try {
            return (Class<? extends Message>) object.getClass().getMethod(methodName).getReturnType();
        } catch (NoSuchMethodException e) {
            return Message.class;
        }
    }

    /**
     * Extract Protobuf message type from collection.
     *
     * @param object     Object that contains collection of Protobuf messages.
     * @param methodName getter method name.
     * @return Class of the Protobuf message.
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Message> getMessageCollectionType(final Object object, final String methodName) {
        try {
            ParameterizedType stringListType = (ParameterizedType) object
                .getClass()
                .getMethod(methodName)
                .getGenericReturnType();
            return (Class<? extends Message>) stringListType.getActualTypeArguments()[0];
        } catch (NoSuchMethodException e) {
            return Message.class;
        }
    }
}
