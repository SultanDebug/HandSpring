package webMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by sultan on 2018/7/22.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    String value() default "";
}
