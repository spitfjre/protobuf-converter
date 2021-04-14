package net.badata.protobuf.example.client.domain;

import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.example.proto.Book;

/**
 * @author jsjem
 * @author Roman Gushel
 */
@Getter
@ProtoClass(value = Book.class, mapper = BookMapper.class)
@Setter
public class ReadingBook {

    @ProtoField(name = "id")
    private long bookId;

    @ProtoField
    private String author;

    @ProtoField
    private String title;

    @ProtoField
    private int pages;

    @ProtoField(name = "state", converter = BooleanEnumConverter.class)
    private boolean available;

    @ProtoField(name = "name")
    private String ownerName;

    @Override
    public String toString() {
        return title + " by " + author + ". - " + pages;
    }
}
