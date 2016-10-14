package com.chenanze.prodmapper.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by duian on 2016/10/12.
 */

public class Log {

    public enum LOG_STATUS {
        DEVELOP, RELEASE
    }

    public static LOG_STATUS mLogStatus = LOG_STATUS.DEVELOP;

    private static Messager mMessager;

    public static void init(Messager messager) {
        mMessager = messager;
    }

    public static void setLogStatus(LOG_STATUS logStatus) {
        mLogStatus = logStatus;
    }

    public static void printLog(String content) {
        if (mLogStatus == LOG_STATUS.DEVELOP) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, content);
        }
    }

    public static void printWarning(String content) {
        mMessager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, content);
    }

    public static void printError(String content) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, content);
    }
}
