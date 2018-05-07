package cqf.hn.pastedata;// PackageElement


/**
 * Created by cqf on 2017/8/25 16:48
 * <p>
 * 对Element的一些理解：
 * 在Java中一个类可以用元素（Element）来表示
 * 被注解注释的元素会被传递给AbstractProcessor的process方法中的roundEnvironment.getElementsAnnotatedWith(注解.class)的集合中
 * 例如@SrcClass注释了DstData
 * 那么roundEnvironment.getElementsAnnotatedWith(SrcClass.class)的元素集合中必定有个元素（Element为TypeElement）
 * 那么如果遍历roundEnvironment.getElementsAnnotatedWith(SrcClass.class)，由元素（Element）ele.getEnclosingElement()->即获得PackageElement（cqf.hn.pastedata对于的元素）
 * <p>
 * 例如@DifGetMethod注释了方法setId
 * 那么roundEnvironment.getElementsAnnotatedWith(DifGetMethod.class)的元素集合中必定有个元素（Element为ExecuteableElement）
 * 那么如果遍历roundEnvironment.getElementsAnnotatedWith(DifGetMethod.class)，由元素（Element）ele.getEnclosingElement()->即获得TypeElement（cqf.hn.pastedata.DstData对于的元素）
 * ele.getEnclosingElement().getEnclosingElement()->即获得PackageElement（cqf.hn.pastedata对于的元素）
 */

import android.util.Log;

import cqf.hn.pastedata.lib.annotation.DifGetMethod;
import cqf.hn.pastedata.lib.annotation.SrcClass;

/**
 * set方法必须提供
 */
@SrcClass({SrcData1.class})//* 注解被映射的类
public class DstData {// TypeElement

    public DstData() { // ExecuteableElement
        //String as = null;
        Log.v("shan", toString());
        // as.toLowerCase();
    }

    private String id; // VariableElement
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

    /**
     * 切割set的方法后
     * 有is开头：
     * 只剩下is(只要有第二个字母大写：不变，否则都转换成小写)
     * is与与其他单词拼接(只要有第二个字母大写：不变)
     * 如果没有is开头
     * 添加is在开头
     */

    //* 此处MainActivity的作用与SrcData1类似
    @DifGetMethod({MainActivity.class})
    public void setId(String id) {
        this.id = id;
    }

    //* 标记get对应的get方法或者没有
    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class}, method_name = {"", "getType1", "getType2"})
    public void setType(String type) {
        this.type = type;
    }

    @DifGetMethod(value = {MainActivity.class})
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @DifGetMethod(value = {MainActivity.class})
    public void setTitle(String title) {
        this.title = title;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setA(int a) {
        this.a = a;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setSrcData1(SrcData1 srcData1) {
        this.srcData1 = srcData1;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setISHas(boolean ISHas) {
        this.ISHas = ISHas;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setiSHAS(boolean iSHAS) {
        this.iSHAS = iSHAS;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setHas(boolean has) {
        IsHas = has;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setiS(boolean iS) {
        this.iS = iS;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setIS(boolean IS) {
        this.IS = IS;
    }

    @DifGetMethod(value = {MainActivity.class, SrcData1.class, SrcData2.class})
    public void setIs(boolean is) {
        this.is = is;
    }


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public int getA() {
        return a;
    }

    public SrcData1 getSrcData1() {
        return srcData1;
    }

    public boolean ISHas() {
        return ISHas;
    }

    public boolean iSHAS() {
        return iSHAS;
    }

    public boolean isHas() {
        return IsHas;
    }

    public boolean iS() {
        return iS;
    }

    public boolean IS() {
        return IS;
    }

    public boolean is() {
        return is;
    }

    /**
     * get/set 命名规则
     * 一般JavaBean属性以小写字母开头，驼峰命名格式，相应的 getter/setter 方法是 get/set 接上首字母大写的属性名。例如：属性名为userName，其对应的getter/setter 方法是 getUserName/setUserName。
     * <p>
     * 但是，还有一些特殊情况：
     * <p>
     * 1、如果属性名的第二个字母大写，那么该属性名直接用作 getter/setter 方法中 get/set 的后部分，就是说大小写不变。例如属性名为uName，方法是getuName/setuName。
     * <p>
     * 2、如果前两个字母是大写（一般的专有名词和缩略词都会大写），也是属性名直接用作 getter/setter 方法中 get/set 的后部分。例如属性名为URL，方法是getURL/setURL。
     * <p>
     * 3、如果首字母大写，也是属性名直接用作 getter/setter 方法中 get/set 的后部分。例如属性名为Name，方法是getName/setName，这种是最糟糕的情况，会找不到属性出错，因为默认的属性名是name。
     * <p>
     * 所以在action的全局变量和JavaBean命名时应该注意符合以上命名规范。
     */


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

    public class InnerClass {/*测试*/
        private String name;
        private String des;
        private String size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
