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

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        RequestContext ctx = new RequestContext();

        String userId = request.getHeader(HEADER_USER_ID);
        if (StringUtils.isNotBlank(userId) && StringUtils.isBlank(userId)) {
            ctx.setUserId(Long.parseLong(userId));
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
