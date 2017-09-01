package cqf.hn.pastedata.lib.util;

/**
 * Created by cqf on 2017/8/31 11:43
 */
public class StringUtil {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(CharSequence... strs) {
        if (strs == null || strs.length == 0)
            return true;
        else
            for (CharSequence str : strs) {
                if (!isEmpty(str)) {
                    return false;
                }
            }
        return true;
    }
}
