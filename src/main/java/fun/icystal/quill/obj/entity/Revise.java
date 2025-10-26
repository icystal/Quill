package fun.icystal.quill.obj.entity;

import java.time.LocalDateTime;

public record Revise(Long reviseId,
                     Long bookId,
                     Long ctxId,
                     String content,
                     LocalDateTime time
                     ) {
}
