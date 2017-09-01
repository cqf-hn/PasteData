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

/**
 * set方法必须提供
 */
@SrcClass(value = {SrcData1.class})
public class DstData {// TypeElement

    public DstData() { // ExecuteableElement
        System.out.println(toString());
        PasteData.getInstance().paste(this);
    }

    private String id; // VariableElement
    @DifField(value = {SrcData1.class, SrcData2.class, SrcData3.class}, name = {"type1", "type2", "type3"})
    private String type;
    private String desc;
    private String title;
    /*测试*/
    private int a;
    private SrcData1 srcData1;


    private boolean ISHas;
    private boolean iSHAS;
    private boolean IsHas;

    private boolean iS;
    private boolean IS;
    private boolean is;
    private boolean Is;

    private boolean isHas;

    private boolean has;

    public boolean isHas() {
        return has;
    }

    public void setHas(boolean has) {
        this.has = has;
    }

    public boolean iSHAS() {
        return iSHAS;
    }

    public void setiSHAS(boolean iSHAS) {
        this.iSHAS = iSHAS;
    }



    public boolean ISHas() {
        return ISHas;
    }

    public void setISHas(boolean ISHas) {
        this.ISHas = ISHas;
    }

    public boolean iS() {
        return iS;
    }

    public void setiS(boolean iS) {
        this.iS = iS;
    }

    public boolean IS() {
        return IS;
    }

    public void setIS(boolean IS) {
        this.IS = IS;
    }

    public boolean is() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }


    /**
     * 切割set的方法后
     * 有is开头：
     *        只剩下is(只要有第二个字母大写：不变，否则都转换成小写)
     *        is与与其他单词拼接(只要有第二个字母大写：不变)
     * 如果没有is开头
     *        添加is在开头
     */



    /**
     * get/set 命名规则
     * 一般JavaBean属性以小写字母开头，驼峰命名格式，相应的 getter/setter 方法是 get/set 接上首字母大写的属性名。例如：属性名为userName，其对应的getter/setter 方法是 getUserName/setUserName。
     *
     * 但是，还有一些特殊情况：
     *
     * 1、如果属性名的第二个字母大写，那么该属性名直接用作 getter/setter 方法中 get/set 的后部分，就是说大小写不变。例如属性名为uName，方法是getuName/setuName。
     *
     * 2、如果前两个字母是大写（一般的专有名词和缩略词都会大写），也是属性名直接用作 getter/setter 方法中 get/set 的后部分。例如属性名为URL，方法是getURL/setURL。
     *
     * 3、如果首字母大写，也是属性名直接用作 getter/setter 方法中 get/set 的后部分。例如属性名为Name，方法是getName/setName，这种是最糟糕的情况，会找不到属性出错，因为默认的属性名是name。
     *
     * 所以在action的全局变量和JavaBean命名时应该注意符合以上命名规范。
     */



    String getId() {
        return "";
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

    public int getA() {
        return a;
    }

    public SrcData1 getSrcData1() {
        return srcData1;
    }

    public void setSrcData1(SrcData1 srcData1) {
        this.srcData1 = srcData1;
    }

    public void setData(SrcData1 srcData1, int a, String desc) throws ClassCastException {
        this.a = a;
        this.srcData1 = srcData1;
        this.desc = desc;
    }

    public SrcData1 getData(SrcData1 srcData1, int a, String desc) throws ClassCastException {
       return new SrcData1();
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
