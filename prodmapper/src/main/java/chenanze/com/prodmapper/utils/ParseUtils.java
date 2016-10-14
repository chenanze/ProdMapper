package chenanze.com.prodmapper.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duian on 2016/10/13.
 */

public class ParseUtils {

    private static final String TAG = "ParseUtils";

    private static String mCustomProxyClassName;

    public static String parseCustomProxyClassName(String customProxyClassName ,String originClassName, String targetClassName) {

        mCustomProxyClassName = customProxyClassName;

        String templete = "^(.*)(\\$[O|T])(.*)(\\$[O|T])(.*)$";
        Pattern compile = Pattern.compile(templete);
        Matcher matcher = compile.matcher(mCustomProxyClassName);
        if (!matcher.matches()) {
            printNotMatchErrorMessage();
        }

        String group2 = matcher.group(2);
        String group4 = matcher.group(4);
        if (group2.matches("\\$[O]") && group4.matches("\\$[T]")) {
            group2 = originClassName;
            group4 = targetClassName;
        } else if (group2.matches("\\$[T]") && group4.matches("\\$[O]")) {
            group2 = targetClassName;
            group4 = originClassName;
        } else {
            printNotMatchErrorMessage();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(matcher.group(1));
        stringBuilder.append(group2);
        stringBuilder.append(matcher.group(3));
        stringBuilder.append(group4);
        stringBuilder.append(matcher.group(5));
        return stringBuilder.toString();
    }

    private static void printNotMatchErrorMessage() {
        String errorMessage = "Please check your ProxyClassName,the " + mCustomProxyClassName + " is not match the rule.";
        Log.e(TAG, "printNotMatchErrorMessage: " + errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }
}
