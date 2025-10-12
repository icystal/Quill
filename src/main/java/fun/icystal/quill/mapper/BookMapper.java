package fun.icystal.quill.mapper;

import fun.icystal.quill.obj.entity.Book;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BookMapper {

    @Insert("INSERT INTO book(id, status, book_detail) VALUES (#{id}, #{status}, #{bookDetail})")
    int insert(Book book);

    @Delete("DELETE FROM book WHERE id = #{id}")
    int delete(@Param("id") Long bookId);

    @Update("UPDATE book SET status = #{status}, book_detail = #{bookDetail} WHERE id = #{id}")
    int update(Book book);

    @Select("SELECT * FROM book WHERE id = #{id}")
    Book select(@Param("id") Long bookId);
}
