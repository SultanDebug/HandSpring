package webMvc.annotation;

import java.lang.annotation.*;

/**
 * Created by sultan on 2018/7/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MyEntity {
    String value() default "";
}
