package com.chenanze.prodmapper.utils;

/**
 * Created by duian on 2016/10/20.
 */

public class OriginClassAccessStatus {
    private enum AccessStatus {
        PRIVATE, PUBLIC, PROTECTED, PACKAGE
    }

    private AccessStatus mAccessStatus;

    public AccessStatus getAccessStatus() {
        return mAccessStatus;
    }

    public void setAccessStatus(AccessStatus mAccessStatus) {
        this.mAccessStatus = mAccessStatus;
    }
}
