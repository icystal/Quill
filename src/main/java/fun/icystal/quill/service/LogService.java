package fun.icystal.quill.service;

import fun.icystal.quill.context.ContextHolder;
import fun.icystal.quill.context.RequestContext;
import fun.icystal.quill.mapper.ReviseMapper;
import fun.icystal.quill.obj.entity.Revise;
import fun.icystal.quill.obj.request.QuillRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import util.SnowFlake;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class LogService {

    @Resource
    private ReviseMapper reviseMapper;

    public void saveRevise(QuillRequest request) {
        try {
            RequestContext context = ContextHolder.get();
            Revise revise = new Revise(SnowFlake.flake(), context.getBookId(), context.getCtxId(), request.getAdvice(), LocalDateTime.now());
            reviseMapper.insert(revise);
        } catch (Exception e) {
            log.error("[LogService] 保存修改建议异常", e);
        }
    }

    public List<Revise> queryRevise() {
        try {
            RequestContext context = ContextHolder.get();
            if (context.getBookId() == null || context.getCtxId() == null) {
                return Collections.emptyList();
            }
            return reviseMapper.selectByCtxId(context.getBookId(), context.getCtxId(), 7);
        } catch (Exception e) {
            log.error("[LogService] 查询修改建议异常", e);
            return Collections.emptyList();
        }
    }

}
