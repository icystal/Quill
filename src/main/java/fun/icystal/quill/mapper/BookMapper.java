package fun.icystal.quill.mapper;

import fun.icystal.quill.obj.entity.Book;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BookMapper {

    @Insert("INSERT INTO book(id, book_detail) VALUES (#{id}, #{bookDetail})")
    int insert(Book book);

    @Delete("DELETE FROM book WHERE id = #{id}")
    int delete(@Param("id") Long bookId);

    @Update("UPDATE book SET book_detail = #{bookDetail} WHERE id = #{id}")
    int update(Book book);

    @Select("SELECT * FROM book WHERE id = #{id}")
    Book select(@Param("id") Long bookId);
}
