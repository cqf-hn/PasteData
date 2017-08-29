package cqf.hn.pastedata.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by cqf on 2017/8/25 18:05
 */
@Retention(CLASS) @Target(FIELD)
public @interface DifField {
    Class[] value() default { Object.class };
    String[] name() default {""};
}
