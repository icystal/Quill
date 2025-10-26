package fun.icystal.quill.interceptor;

import fun.icystal.quill.context.ContextHolder;
import fun.icystal.quill.context.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JsonUtil;

@Slf4j
@Component
public class ContextInterceptor implements HandlerInterceptor {

    private static final String HEADER_USER_ID = "Q-USER-ID";

    private static final String HEADER_CTX_ID = "Q-CTX-ID";

    private static final String HEADER_BOOK_ID = "Q-BOOK-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        RequestContext ctx = new RequestContext();

        String userId = request.getHeader(HEADER_USER_ID);
        if (StringUtils.isNotBlank(userId)) {
            ctx.setUserId(Long.parseLong(userId));
        }

        String bookId = request.getHeader(HEADER_BOOK_ID);
        if (StringUtils.isNotBlank(bookId)) {
            ctx.setBookId(Long.parseLong(bookId));
        } else {
            log.warn("[请求上下文拦截器] request did not carry the specified request header: Q-BOOK-ID");
            return false;
        }

        String ctxId = request.getHeader(HEADER_CTX_ID);
        if (StringUtils.isNotBlank(ctxId)) {
            ctx.setCtxId(Long.parseLong(ctxId));
        }


        ContextHolder.set(ctx);
        log.info("[请求上下文拦截器] request context: {}", JsonUtil.toJSONString(ctx));
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        ContextHolder.remove();
    }
}
