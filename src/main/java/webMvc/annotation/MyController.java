package webMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by sultan on 2018/7/22.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {
    String value() default "";
}
