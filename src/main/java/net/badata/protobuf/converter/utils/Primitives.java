/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.badata.protobuf.converter.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;

/**
 * Contains static utility methods pertaining to primitive types and their corresponding wrapper
 * types.
 *
 * @author Kevin Bourrillion
 * @since 1.0
 */
@UtilityClass
public final class Primitives {

    /**
     * A map from primitive types to their corresponding wrapper types.
     */
    private final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE = constructPrimToWrap();

    /**
     * A map from wrapper types to their corresponding primitive types.
     */
    private final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE = constructWrapToPrim();

    private Map<Class<?>, Class<?>> constructPrimToWrap() {
        final Map<Class<?>, Class<?>> primToWrap = new HashMap<>(16);

        primToWrap.put(boolean.class, Boolean.class);
        primToWrap.put(byte.class, Byte.class);
        primToWrap.put(char.class, Character.class);
        primToWrap.put(double.class, Double.class);
        primToWrap.put(float.class, Float.class);
        primToWrap.put(int.class, Integer.class);
        primToWrap.put(long.class, Long.class);
        primToWrap.put(short.class, Short.class);
        primToWrap.put(void.class, Void.class);

        return Collections.unmodifiableMap(primToWrap);
    }

    private Map<Class<?>, Class<?>> constructWrapToPrim() {
        final Map<Class<?>, Class<?>> wrapToPrim = new HashMap<>(16);

        wrapToPrim.put(Boolean.class, boolean.class);
        wrapToPrim.put(Byte.class, byte.class);
        wrapToPrim.put(Character.class, char.class);
        wrapToPrim.put(Double.class, double.class);
        wrapToPrim.put(Float.class, float.class);
        wrapToPrim.put(Integer.class, int.class);
        wrapToPrim.put(Long.class, long.class);
        wrapToPrim.put(Short.class, short.class);
        wrapToPrim.put(Void.class, void.class);

        return Collections.unmodifiableMap(wrapToPrim);
    }

    /**
     * Returns an immutable set of all nine primitive types (including {@code
     * void}). Note that a simpler way to test whether a {@code Class} instance is a member of this
     * set is to call {@link Class#isPrimitive}.
     *
     * @return class set
     * @since 3.0
     */
    public Set<Class<?>> allPrimitiveTypes() {
        return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
    }

    /**
     * Returns an immutable set of all nine primitive-wrapper types (including {@link Void}).
     *
     * @return class set
     * @since 3.0
     */
    public Set<Class<?>> allWrapperTypes() {
        return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
    }

    /**
     * Returns {@code true} if {@code type} is one of the nine primitive-wrapper types, such as
     * {@link Integer}.
     *
     * @param type class
     * @return true if wrapped type
     * @see Class#isPrimitive
     */
    public boolean isWrapperType(Class<?> type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(type);
    }

    /**
     * Returns the corresponding wrapper type of {@code type} if it is a primitive type; otherwise
     * returns {@code type} itself. Idempotent.
     * <pre>
     *     wrap(int.class) == Integer.class
     *     wrap(Integer.class) == Integer.class
     *     wrap(String.class) == String.class
     * </pre>
     *
     * @param type type
     * @param <T>  class
     * @return itself.
     */
    public <T> Class<T> wrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> wrapped = (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.get(type);
        return (wrapped == null) ? type : wrapped;
    }

    /**
     * Returns the corresponding primitive type of {@code type} if it is a wrapper type; otherwise
     * returns {@code type} itself. Idempotent.
     * <pre>
     *     unwrap(Integer.class) == int.class
     *     unwrap(int.class) == int.class
     *     unwrap(String.class) == String.class
     * </pre>
     *
     * @param type type
     * @param <T>  class
     * @return itself.
     */
    public <T> Class<T> unwrap(Class<T> type) {
        // cast is safe: long.class and Long.class are both of type Class<Long>
        @SuppressWarnings("unchecked")
        Class<T> unwrapped = (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return (unwrapped == null) ? type : unwrapped;
    }
}
