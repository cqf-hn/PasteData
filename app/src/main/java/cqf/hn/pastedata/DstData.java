package cqf.hn.pastedata;// PackageElement

import cqf.hn.pastedata.lib.annotation.DifField;
import cqf.hn.pastedata.lib.PasteData;
import cqf.hn.pastedata.lib.annotation.SrcClass;

/**
 * Created by cqf on 2017/8/25 16:48
 * 在Java中一个类可以用元素（Element）来表示
 * 被注解注释的元素会被传递给AbstractProcessor的process方法中的roundEnvironment.getElementsAnnotatedWith(注解.class)的集合中
 * 例如@SrcClass注释了DstData
 * 那么roundEnvironment.getElementsAnnotatedWith(SrcClass.class)的元素集合中必定有个元素（Element为TypeElement）
 * 那么如果遍历roundEnvironment.getElementsAnnotatedWith(SrcClass.class)，由元素（Element）ele.getEnclosingElement()->即获得PackageElement（cqf.hn.pastedata对于的元素）
 * <p>
 * 例如@DifField注释了成员变量id
 * 那么roundEnvironment.getElementsAnnotatedWith(DifField.class)的元素集合中必定有个元素（Element为VariableElement）
 * 那么如果遍历roundEnvironment.getElementsAnnotatedWith(DifField.class)，由元素（Element）ele.getEnclosingElement()->即获得TypeElement（cqf.hn.pastedata.DstData对于的元素）
 * ele.getEnclosingElement().getEnclosingElement()->即获得PackageElement（cqf.hn.pastedata对于的元素）
 */

@SrcClass(value = {SrcData1.class})
public class DstData {// TypeElement

    public DstData() { // ExecuteableElement
        System.out.println(toString());
        PasteData.getInstance().paste(this);
    }

    private String id; // VariableElement
    @DifField(value = {SrcData1.class, SrcData2.class, SrcData3.class}, name = {"type1", "type2","type3"})
    private String type;
    private String desc;
    private String title;
    /*测试*/
    private int a;
    private SrcData1 srcData1;


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

    public void setA(int newA) {// ExecuteableElement
        a = newA;
    }

    @SrcClass(value = {SrcData1.class})
    public class InnerClass {/*测试*/
        private String name;
        private String des;
        private String size;
    }
}
