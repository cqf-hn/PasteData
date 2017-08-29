Src:源数据的类 Dst:目标数据的类
1. 确定如何传输赋值
      提供一个公共方法，必须调用        PasteData.getInstance().paste(this);


2. 两个数据包装类的
     字段名一致

     字段名不一致
         类似于butterknife的@OnClick(一个类(包含Src类的class和字段的string字符串))


3. 准备获取Src和Dst的字段，调用set的方法


butterknife 之所以能够被调用是因为Butterknife.bind(this) 和 Butterknife.unbind(this)

4. 多个数据之间的转化