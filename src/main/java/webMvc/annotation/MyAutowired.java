package webMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by sultan on 2018/7/22.
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyAutowired {
    String value() default "";
}
