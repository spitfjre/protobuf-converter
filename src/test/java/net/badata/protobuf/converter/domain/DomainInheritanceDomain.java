package net.badata.protobuf.converter.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.converter.proto.DomainInheritanceProto;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class DomainInheritanceDomain {

    @Getter
    @ProtoClass(DomainInheritanceProto.Test.class)
    @Setter
    public static class Test extends InheritedByTest {

        @ProtoField
        private Long ownLong;

        @ProtoField
        private double ownDouble;
    }

    @Getter
    @Setter
    public static class InheritedByTest {

        @ProtoField
        private int inheritedInt;

        @ProtoField
        private Float inheritedFloat;
    }
}
