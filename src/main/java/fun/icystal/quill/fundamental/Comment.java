package fun.icystal.quill.fundamental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于类或者字段上
 * 用于给模型自动生成提示词
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {

    String desc();

    String example() default "";

}
