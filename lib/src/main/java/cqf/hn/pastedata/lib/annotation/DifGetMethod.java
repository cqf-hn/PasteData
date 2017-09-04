package cqf.hn.pastedata.lib.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by cqf on 2017/8/25 18:05
 */
@Target(METHOD)
@Retention(CLASS)
public @interface DifGetMethod {
    Class[] value() default {Object.class};

    String[] method_name() default {""};
}
