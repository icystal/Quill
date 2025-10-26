package fun.icystal.quill.mapper;

import fun.icystal.quill.obj.entity.Revise;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReviseMapper {
    @Delete("DELETE FROM revise WHERE ctx_id = #{ctxId}")
    int deleteByCtxId(@Param("ctxId") String ctxId);

    @Insert("INSERT INTO revise(revise_id, book_id, ctx_id, content, time) VALUES(#{reviseId}, #{bookId}, #{ctxId}, #{content}, #{time})")
    int insert(Revise revise);

    @Select("SELECT * FROM revise WHERE book_id = #{bookId} AND ctx_id = #{ctxId} ORDER BY time DESC LIMIT #{limit}")
    List<Revise> selectByCtxId(Long bookId, Long ctxId, int limit);
}
