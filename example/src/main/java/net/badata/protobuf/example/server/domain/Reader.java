package net.badata.protobuf.example.server.domain;

import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.example.proto.User;

/**
 * @author jsjem
 * @author Roman Gushel
 */
@Getter
@ProtoClass(value = User.class)
@Setter
public class Reader {

    @ProtoField
    private String name;

    @ProtoField
    private String password;
}
