Src:源数据的类 Dst:目标数据的类
1. 确定如何传输赋值
      提供一个公共方法，必须调用        PasteData.getInstance().paste(this);


2. 两个数据包装类的
     字段名一致

     字段名不一致
         类似于butterknife的@OnClick(一个类(包含Src类的class和字段的string字符串))


3. 准备获取Src和Dst的字段，调用set的方法


butterknife 之所以能够被调用是因为Butterknife.bind(this) 和 Butterknife.unbind(this)



问题：
    1. 多个数据之间的转化

步骤：
    1.获取被注解的类的成员变量
    2.以被注解的成员变量的String查找SrcClass中的类的成员变量,
        以DifField中的数值进行一一对应查找（查找不到报错）
        DifField的value值只能是被包含SrcClass的vallue值（如果不是就报错）
    3.如果能够在SrcClass中的类找到对应的成员变量，就为被注解的类赋值

细节：
    1.FieldDesc用于拼接源数据的字段



杂乱的思路：
    将DifField注解改为DifGetMethod(每个PasteClass可能有多个DifGetMethod)
    每个PasteClass有个HasMap：key->Dst中对应的方法名;value->DifGetMethod
    DifGetMethod中有个HasMap:key->ClassPath;key->Src中get方法(如果Src中没有对应的get的方法，设置为空字符串，或者不填
    ，则赋值时，不调用对应的set方法)
    DifGetMethod中的类不存在SrcClass中的类，不做处理
