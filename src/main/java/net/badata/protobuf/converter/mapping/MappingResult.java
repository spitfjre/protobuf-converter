package net.badata.protobuf.converter.mapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents results of field mapping performed by {@link net.badata.protobuf.converter.mapping.Mapper} implementation.
 *
 * @author jsjem
 * @author Roman Gushel
 */
@Getter
@RequiredArgsConstructor
public class MappingResult {

    private final Result code;
    private final Object value;
    private final Object destination;

    /**
     * Enum of mapping results.
     */
    public enum Result {
        /**
         * Single simple field is mapped.
         */
        MAPPED,
        /**
         * Single complex field is mapped.
         */
        NESTED_MAPPING,
        /**
         * Collection field is mapped.
         */
        COLLECTION_MAPPING,
    }
}
