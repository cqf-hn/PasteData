package cqf.hn.pastedata.lib;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by cqf on 2017/8/25 17:47
 */
@Retention(CLASS) @Target(CONSTRUCTOR)
public @interface SrcClass {
    Class[] value() default { Object.class };
}
