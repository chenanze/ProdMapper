package com.chenanze.prodmapper.utils;

import javax.lang.model.element.Element;

/**
 * Created by duian on 2016/10/20.
 */

public class OriginClassAccessStatus {
    public enum AccessStatus {
        PRIVATE, PUBLIC, PROTECTED, PACKAGE
    }

    public enum ElementType {
        METHOD, PARAMETER
    }


    private Element mElement;

    private ElementType mElementType;

    private AccessStatus mAccessStatus;

    private boolean mIsParameterNameMatched = false;

    private boolean mIsParameterTypeMatched = false;

    private boolean mIsGetMethodNameMatched = false;

    private boolean mIsGetMethodRetrunTypeMatched = false;

    private boolean mIsAccessAble;

    public Element getElement() {
        return mElement;
    }

    public void setElement(Element mElement) {
        this.mElement = mElement;
    }

    public String getElementName() {
        return this.mElement.getSimpleName().toString();
    }

    public ElementType getElementType() {
        return mElementType;
    }

    public void setElementType(ElementType mElementType) {
        this.mElementType = mElementType;
    }

    public AccessStatus getAccessStatus() {
        return mAccessStatus;
    }

    public void setAccessStatus(AccessStatus mAccessStatus) {
        this.mAccessStatus = mAccessStatus;
    }

    public boolean isIsParameterMatched() {
        return mIsParameterNameMatched;
    }

    public void setIsParameterNameMatched(boolean mIsParameterMatched) {
        this.mIsParameterNameMatched = mIsParameterMatched;
    }

    public boolean isIsParameterTypeMatched() {
        return mIsParameterTypeMatched;
    }

    public void setIsParameterTypeMatched(boolean mIsParameterTypeMatched) {
        this.mIsParameterTypeMatched = mIsParameterTypeMatched;
    }

    public boolean isGetMethodNameMatched() {
        return mIsGetMethodNameMatched;
    }

    public void setIsGetMethodNameMatched(boolean mIsGetMethodNameMatched) {
        this.mIsGetMethodNameMatched = mIsGetMethodNameMatched;
    }

    public boolean isGetMethodRetrunTypeMatched() {
        return mIsGetMethodRetrunTypeMatched;
    }

    public void setIsGetMethodRetrunTypeMatched(boolean mIsGetMethodRetrunTypeMatched) {
        this.mIsGetMethodRetrunTypeMatched = mIsGetMethodRetrunTypeMatched;
    }

    public boolean isAccessAble() {
        if (((mIsGetMethodNameMatched && mIsGetMethodRetrunTypeMatched) || (mIsParameterNameMatched && mIsParameterTypeMatched)) && mAccessStatus == AccessStatus.PUBLIC)
            mIsAccessAble = true;
        else
            mIsAccessAble = false;
        return mIsAccessAble;
    }
}
