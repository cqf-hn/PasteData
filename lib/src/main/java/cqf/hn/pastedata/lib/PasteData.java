package cqf.hn.pastedata.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cqf on 2017/8/25 17:40
 */
public class PasteData {

    private static Map<Class<?>, DataPaster<Object>> pasterMap = new HashMap<>();

    private PasteData() {
    }

    public static PasteData getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        public final static PasteData instance = new PasteData();
    }

    public void paste(Object dstData, Object srcData) {
        if (srcData==null){
            throw new NullPointerException();
        }
        Class<?> targetClass = dstData.getClass();
        try {
            DataPaster<Object> dataPaster = findDataPasterForClass(targetClass);
            if (dataPaster != null) {
                dataPaster.paste(dstData, srcData);
            }
        } catch (Exception e) {
            //className抛出异常是否使用该注解
            throw new RuntimeException("Unable to paste data for " + targetClass.getName(), e);
        }
    }

    public void unpaste(Object dstData) {
        Class<?> targetClass = dstData.getClass();
        try {
            DataPaster<Object> dataPaster = findDataPasterForClass(targetClass);
            if (dataPaster != null) {
                dataPaster.unpaste(dstData);
            }
        } catch (Exception e) {
            //className抛出异常是否使用该注解
            throw new RuntimeException("Unable to unpaste data for " + targetClass.getName(), e);
        }
    }



    private DataPaster<Object> findDataPasterForClass(Class<?> cls)
            throws IllegalAccessException, InstantiationException {
        DataPaster<Object> dataPaster = pasterMap.get(cls);
        if (dataPaster != null) {
            return dataPaster;
        }
        String clsName = cls.getName();
        if (clsName.startsWith(PasteDataProcessor.ANDROID_PREFIX) || clsName.startsWith(PasteDataProcessor.JAVA_PREFIX)) {
            return NOP_DATA_PASTER;
        }
        try {
            Class<?> dataPasterClass = Class.forName(clsName.concat(PasteDataProcessor.SUFFIX));
            //noinspection unchecked
            dataPaster = (DataPaster<Object>) dataPasterClass.newInstance();
        } catch (ClassNotFoundException e) {
            dataPaster = findDataPasterForClass(cls.getSuperclass());
        }
        pasterMap.put(cls, dataPaster);
        return dataPaster;
    }

    static final DataPaster<Object> NOP_DATA_PASTER = new DataPaster<Object>() {
        @Override
        public void paste(Object target, Object source) {
        }

        @Override
        public void unpaste(Object target) {
        }
    };
}
