package cqf.hn.pastedata.lib;

/**
 * Created by cqf on 2017/8/25 17:40
 */
public class PasteData {
    private PasteData() {
    }

    public static PasteData getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public final static PasteData instance = new PasteData();
    }

    public void paste(Object data){

    }
}
