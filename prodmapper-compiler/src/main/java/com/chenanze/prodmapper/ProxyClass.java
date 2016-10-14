package com.chenanze.prodmapper;

import com.chenanze.prodmapper.utils.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by duian on 2016/10/11.
 */

public class ProxyClass {

    private static final String PACKAGE_NAME = "com.prodmapper";

    private static final String SUFFIX = "$$MapperBind";

    private static Elements mElementsUtils;

    private TypeElement mTargetTypeElement;

    private ExecutableElement mConstructorElement;

    private final TypeElement mOriginTypeElement;

    private String mProxyClassNameString;

    private ClassName mTargetClassName;

    private ClassName mOriginClassName;

    private final String mCustomProxyClassNameString;

    public ProxyClass(TypeElement orginTypeElement, String customProxyClassNameString) {
        mOriginTypeElement = orginTypeElement;
        mCustomProxyClassNameString = customProxyClassNameString;
    }

    public void setTargetTypeElement(TypeElement mTargetTypeElement) {
        this.mTargetTypeElement = mTargetTypeElement;
    }

    public JavaFile generateSourceCode() {
        Log.printLog("mCustomProxyClassNameString: " + mCustomProxyClassNameString);
        if (!mCustomProxyClassNameString.equals("NONE")) {
            mProxyClassNameString = parseCustomProxyClassName(getClassName(mOriginTypeElement), getClassName(mTargetTypeElement));
        } else {
            mProxyClassNameString = getClassName(mOriginTypeElement) + "__" + getClassName(mTargetTypeElement) + SUFFIX;
        }
//        Log.printLog("getPackageName: " + getPackageName(mOriginTypeElement));
        mTargetClassName = ClassName.get(getPackageName(mTargetTypeElement), getClassName(mTargetTypeElement));
        mOriginClassName = ClassName.get(getPackageName(mOriginTypeElement), getClassName(mOriginTypeElement));

        TypeSpec.Builder builder = TypeSpec.classBuilder(mProxyClassNameString)
                .addModifiers()
                .addModifiers(Modifier.PUBLIC);
        builder.addSuperinterface(
                ParameterizedTypeName.get(ClassName.get("chenanze.com.prodmapper", "ValueBinder"), mTargetClassName, mOriginClassName));

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("transform");

        String originObject = getVariableName(mOriginTypeElement);

        methodBuilder.addModifiers(Modifier.PUBLIC)
                .addParameter(mOriginClassName, originObject)
                .returns(mTargetClassName);

//        ElementFilter.constructorsIn(mTargetTypeElement.getEnclosedElements());

        StringBuilder parameters = new StringBuilder();
        for (VariableElement variableElement : mConstructorElement.getParameters()) {
            parameters.append(originObject + "." + getGetAttributeMethod(getMethodParameter(variableElement)) + ",");
        }
        parameters.deleteCharAt(parameters.length() - 1);

        methodBuilder.addStatement("return new $T(" + parameters + ")", mTargetClassName);

        builder.addMethod(methodBuilder.build());

        return JavaFile.builder(PACKAGE_NAME, builder.build())
                .addFileComment("Generated code from ProdMapper. Do not modify!")
                .build();
    }

    private String parseCustomProxyClassName(String originClassName, String targetClassName) {
        String templete = "^(.*)(\\$[O|T])(.*)(\\$[O|T])(.*)$";
        Pattern compile = Pattern.compile(templete);
        Matcher matcher = compile.matcher(mCustomProxyClassNameString);
        if (!matcher.matches()) {
            Log.printLog("is not match");
            printNotMatchErrorMessage();
        }
        Log.printLog("is matched");

        String group2 = matcher.group(2);
        String group4 = matcher.group(4);
        if (group2.matches("\\$[O]") && group4.matches("\\$[T]")) {
            Log.printLog("originClassName" + originClassName);
            Log.printLog("targetClassName" + targetClassName);
            group2 = originClassName;
            group4 = targetClassName;
        } else if (group2.matches("\\$[T]") && group4.matches("\\$[O]")) {
            group2 = targetClassName;
            group4 = originClassName;
        } else {
            Log.printLog("is not match");
            printNotMatchErrorMessage();
        }

        Log.printLog("group2" + group2);
        Log.printLog("group4" + group4);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(matcher.group(1));
        stringBuilder.append(group2);
        stringBuilder.append(matcher.group(3));
        stringBuilder.append(group4);
        stringBuilder.append(matcher.group(5));
        return stringBuilder.toString();
    }

    private void printNotMatchErrorMessage() {
        String errorMessage = "Please check your ProxyClassName,the " + mCustomProxyClassNameString + " is not match the rule.";
        Log.printError(errorMessage);
        throw new IllegalArgumentException(errorMessage);
    }

    public String getProxyClassName() {
        return mProxyClassNameString;
    }

    @Override
    public String toString() {
        return "Origin Class: "
                + mOriginTypeElement.getQualifiedName().toString() + "\n"
                + "--> Target Class: "
                + mTargetTypeElement.getQualifiedName().toString();
    }

    public static void setElementsUtils(Elements mElementsUtils) {
        ProxyClass.mElementsUtils = mElementsUtils;
    }

    public String getPackageName(TypeElement typeElement) {
        return mElementsUtils.getPackageOf(typeElement).toString();
    }

    public String getClassName(TypeElement typeElement) {
        return typeElement.getSimpleName().toString();
    }

    public String getVariableName(TypeElement typeElement) {
        StringBuilder stringBuilder = new StringBuilder(getClassName(typeElement));
        stringBuilder.replace(0, 1, String.valueOf(stringBuilder.charAt(0)).toLowerCase());
        return stringBuilder.toString();
    }

    public String getMethodParameter(VariableElement variableElement) {
        return variableElement.getSimpleName().toString();
    }

    public String upperCaseFirstChar(String content) {
        StringBuilder stringBuilder = new StringBuilder(content);
        stringBuilder.replace(0, 1, String.valueOf(stringBuilder.charAt(0)).toUpperCase());
        return stringBuilder.toString();
    }

    public String getGetAttributeMethod(String parameterName) {
        return "get" + upperCaseFirstChar(parameterName) + "()";
    }

    public void setConstructorElement(ExecutableElement mConstructorElement) {
        this.mConstructorElement = mConstructorElement;
    }

    public boolean checkIsProcessCorrect(String targetClassName) {
        if (mConstructorElement == null) {
            Log.printWarning("Target Class: " + targetClassName + " should have a @Construction annotate in it's constructor." );
            return false;
        }
        return true;
    }
}
