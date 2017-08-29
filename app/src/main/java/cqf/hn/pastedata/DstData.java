package cqf.hn.pastedata;

import cqf.hn.pastedata.lib.DifField;
import cqf.hn.pastedata.lib.PasteData;
import cqf.hn.pastedata.lib.SrcClass;

/**
 * Created by cqf on 2017/8/25 16:48
 */

public class DstData {

    @SrcClass(value = {SrcData.class})
    public  DstData(){
        System.out.println(toString());
        PasteData.getInstance().paste(this);
    }

    @DifField(value = {SrcData.class,MainActivity.class},name = {"asd","asdasdasd"})
    private String id;
    private String type;
    private String desc;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "DstData{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
