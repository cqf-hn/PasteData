package cqf.hn.pastedata.lib;

/**
 * Created by cqf on 2017/9/1 13:50
 */
public interface DataPaster<T> {
    void paste(T target, Object source);
    void unpaste(T target);
}
