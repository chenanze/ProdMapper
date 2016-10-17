package chenanze.com.prodmapper;

import android.util.Log;

import com.chenanze.prodmapper.BindType;

import java.util.LinkedHashMap;
import java.util.Map;

import chenanze.com.prodmapper.utils.ParseUtils;

/**
 * Created by duian on 2016/10/10.
 */

public class Prodmapper {

    private static final String PACKAGE_NAME = "com.prodmapper";

    private static final String TAG = "Prodmapper";

    private static final String SUFFIX = "$$MapperBind";

    public static Map<String, ValueBinder> mValueBinderMapper = new LinkedHashMap<>();

    public static Object transfrom(Object orginObject) {
        Class<?> orginObjectClass = orginObject.getClass();
        BindType annotation = orginObjectClass.getAnnotation(BindType.class);
        String customProxyClassName = annotation.proxyClassName();
        String originName = orginObjectClass.getSimpleName();
        String targetName = annotation.value().getSimpleName();

        if (!customProxyClassName.equals("NONE")) {
            String customProxyClassFullName = PACKAGE_NAME + "." + ParseUtils.parseCustomProxyClassName(customProxyClassName, originName, targetName);
            return transfrom(orginObject, customProxyClassFullName);
        }
        return transfrom(orginObject, originName, targetName);
    }

    public static Object transfromWithClazz(Object orginObject, Class<?> originClazz, Class targetClazz) {
        return transfrom(orginObject, originClazz.getSimpleName(), targetClazz.getSimpleName());
    }

    public static Object transfromWithClazz(Object orginObject, Class targetClazz) {
        return transfrom(orginObject, orginObject.getClass().getSimpleName(), targetClazz.getSimpleName());
    }

    private static Object transfrom(Object orginObject, String originClassName, String targetClassName) {
        String proxyClassFullName = PACKAGE_NAME + "." + originClassName + "__" + targetClassName + SUFFIX;
        return transfrom(orginObject, proxyClassFullName);
    }

    private static Object transfrom(Object originObject, String proxyClassFullName) {
        try {
            ValueBinder result = mValueBinderMapper.get(proxyClassFullName);
            if (result == null) {
                ValueBinder valueBinder = (ValueBinder) Class.forName(proxyClassFullName).newInstance();
                mValueBinderMapper.put(proxyClassFullName, valueBinder);
                return valueBinder.transformInto(originObject);
            }
            return result.transformInto(originObject);
//            Log.d(TAG, "BindClassName: " + valueBinder.getClass().getSimpleName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "proxyClassName: " + proxyClassFullName);
        return null;
    }

}
