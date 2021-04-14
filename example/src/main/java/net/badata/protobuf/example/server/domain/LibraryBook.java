package net.badata.protobuf.example.server.domain;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.example.client.domain.BooleanEnumConverter;
import net.badata.protobuf.example.proto.Book;

/**
 * @author jsjem
 * @author Roman Gushel
 */
@Getter
@ProtoClass(Book.class)
@Setter
public class LibraryBook {

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

    @ProtoField
    private Reader owner;

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof LibraryBook) {
            LibraryBook book = (LibraryBook) obj;

            return (
                Objects.equals(book.author, author) &&
                Objects.equals(book.title, title) &&
                Objects.equals(book.bookId, bookId)
            );
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, bookId);
    }
}
