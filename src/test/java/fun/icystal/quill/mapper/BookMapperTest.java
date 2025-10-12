package fun.icystal.quill.mapper;

import fun.icystal.quill.obj.entity.Book;
import fun.icystal.quill.obj.entity.BookBrief;
import fun.icystal.quill.obj.entity.BookDetail;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookMapperTest {

    @Resource
    private BookMapper bookMapper;

    @Test
    @Order(1)
    public void insert() {
        Book book = new Book();
        book.setId(1L);
        book.setStatus(2);
        book.setBookDetail(new BookDetail());

        int insert = bookMapper.insert(book);
        assert insert == 1;
        log.info("insert test end");
    }

    @Test
    @Order(2)
    public void update() {
        Book book = new Book();
        book.setId(1L);

        book.setStatus(3);
        BookBrief brief = new BookBrief();
        brief.setGenre("爱情悬疑");
        if (book.getBookDetail() == null) {
            book.setBookDetail(new BookDetail());
        }
        book.getBookDetail().setBrief(brief);

        int update = bookMapper.update(book);
        assert update == 1;
        log.info("update test end");
    }

    @Test
    @Order(3)
    public void select() {
        Book select = bookMapper.select(1L);
        assert select != null;
        log.info("select test end");
    }

    @Test
    @Order(4)
    public void delete() {
        int delete = bookMapper.delete(1L);
        assert delete == 1;
        log.info("delete test end");
    }

}
