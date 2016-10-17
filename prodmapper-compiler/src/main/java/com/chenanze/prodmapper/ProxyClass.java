package com.chenanze.prodmapper;

import com.chenanze.prodmapper.utils.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import static com.chenanze.prodmapper.ProxyClass.ConstructorParameterState.FILED;
import static com.chenanze.prodmapper.ProxyClass.ConstructorParameterState.GET_METHOD;

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

    private Map<String, ConstructorParameterState> mConsturctorParameterStatesMap = new LinkedHashMap<>();

    enum ConstructorParameterState {
        FILED, GET_METHOD
    }

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

        checkIsConstructorParameterMatched();

        createStaticTransformMethod(builder);
        createTransformIntoMethod(builder);

        return JavaFile.builder(PACKAGE_NAME, builder.build())
                .addFileComment("Generated code from ProdMapper. Do not modify!")
                .build();
    }

    private void checkIsConstructorParameterMatched() {

        List<ExecutableElement> originMethodElements = ElementFilter.methodsIn(mOriginTypeElement.getEnclosedElements());
        List<VariableElement> orginFiledElements = ElementFilter.fieldsIn(mOriginTypeElement.getEnclosedElements());

        for (VariableElement parameter : mConstructorElement.getParameters()) {
            boolean isMatched = false;
            // Check the parameter of the constructor of target class if have the map get method in origin class to match it.
            // If matched, save the state to GET_METHOD.
            for (ExecutableElement methodElement : originMethodElements) {
                String constructorParameterName = getGetAttributeMethod(getMethodParameter(parameter));
                Log.printLog("constructorParameterName: " + constructorParameterName + " methodElement: " + methodElement.getSimpleName() + "()");
                if (getGetAttributeMethod(getMethodParameter(parameter)).equals(methodElement.getSimpleName() + "()")) {
                    isMatched = true;
                    mConsturctorParameterStatesMap.put(parameter.getSimpleName().toString(), GET_METHOD);
                    break;
                }
            }
            Log.printLog("Save the status of parameter: " + parameter.getSimpleName() + " to GET_METHOD");
            if (isMatched) continue;
            // Check the parameter of the constructor of target class if have the map fields in origin class to match it.
            // If matched, save the state to FIELDS.
            for (VariableElement orginFiledElement : orginFiledElements) {
                Log.printLog("constructorParameterName: " + parameter.getSimpleName().toString() + " orginFiledElement: " + orginFiledElement.getSimpleName().toString());
                if (!orginFiledElement.getModifiers().contains(Modifier.PUBLIC)) {
                    String errorMessage = "Please make true the type of access of the field of " + orginFiledElement.getSimpleName()+" in " + getClassName(mOriginTypeElement) + " is public ";
                    Log.printError(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
                if (parameter.getSimpleName().toString().equals(orginFiledElement.getSimpleName().toString())) {

                    isMatched = true;
                    mConsturctorParameterStatesMap.put(parameter.getSimpleName().toString(), FILED);
                    break;
                }
            }
            Log.printLog("Save the status of parameter: " + parameter.getSimpleName() + " to FIELDS.");
            // If not matched, throw exception and show error message.
            if (!isMatched) {
                String errorMessage = "The parameter " + parameter.getSimpleName() + " of constructor " + mConstructorElement.toString() + " can not find the Get Method to match it.";
                Log.printError(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
        Log.printLog("checkIsConstructorParameterMatched ok");
    }

    private void createTransformIntoMethod(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("transformInto");
        createTransformMethod(builder, methodBuilder);
    }

    private void createStaticTransformMethod(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("transform");
        methodBuilder.addModifiers(Modifier.STATIC);
        createTransformMethod(builder, methodBuilder);
    }

    private void createTransformMethod(TypeSpec.Builder builder, MethodSpec.Builder methodBuilder) {
        String originObject = getVariableName(mOriginTypeElement);
        methodBuilder.addModifiers(Modifier.PUBLIC)
                .addParameter(mOriginClassName, originObject)
                .returns(mTargetClassName);
        StringBuilder parameters = new StringBuilder();
        for (VariableElement variableElement : mConstructorElement.getParameters()) {
            ConstructorParameterState constructorParameterState = mConsturctorParameterStatesMap.get(variableElement.getSimpleName().toString());
            switch (constructorParameterState) {
                case FILED:
                    parameters.append(originObject + "." + variableElement.getSimpleName() + ",");
                    break;
                case GET_METHOD:
                    parameters.append(originObject + "." + getGetAttributeMethod(getMethodParameter(variableElement)) + ",");
                    break;
            }
        }
        parameters.deleteCharAt(parameters.length() - 1);
        methodBuilder.addStatement("return new $T(" + parameters + ")", mTargetClassName);
        builder.addMethod(methodBuilder.build());
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
            Log.printWarning("Target Class: " + targetClassName + " should have a @Construction annotate in it's constructor.");
            return false;
        }
        return true;
    }
}
