package cqf.hn.pastedata.lib.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by cqf on 2017/8/25 17:47
 */
@Target(TYPE)
@Retention(CLASS)
public @interface SrcClass {
    Class[] value();
}
