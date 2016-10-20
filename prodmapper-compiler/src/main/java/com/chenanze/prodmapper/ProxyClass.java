package com.chenanze.prodmapper;

import com.chenanze.prodmapper.utils.LinkedListStack;
import com.chenanze.prodmapper.utils.Log;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
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
    private static final String NODE_TOP = "top";

    private static Elements mElementsUtils;

    private TypeElement mTargetTypeElement;

    private ExecutableElement mConstructorElement;

    private final TypeElement mOriginTypeElement;

    private String mProxyClassNameString;

    private ClassName mTargetClassName;

    private ClassName mOriginClassName;

    private final String mCustomProxyClassNameString;

    private Map<String, ConstructorParameterState> mConsturctorParameterStatesMap = new LinkedHashMap<>();
    private Map<String, ConstructorParameterState> mOriginFiledsInSpuerStatesMap = new LinkedHashMap<>();

    private LinkedListStack<Node> mLinkedListStack = new LinkedListStack<>();

    private String mPackageName;
    private ParameterizedTypeName mTargetArrayListClassName;

    enum ConstructorParameterState {
        FILED, GET_METHOD
    }

    public ProxyClass(TypeElement orginTypeElement, String customProxyClassNameString) {
        mOriginTypeElement = orginTypeElement;
        mCustomProxyClassNameString = customProxyClassNameString;
        mPackageName = getPackageName(mOriginTypeElement);
    }

    public void setTargetTypeElement(TypeElement mTargetTypeElement) {
        this.mTargetTypeElement = mTargetTypeElement;
    }

    public JavaFile generateSourceCode() {
        Log.printLog("mCustomProxyClassNameString: " + mCustomProxyClassNameString);

        checkIsConstructorParameterMatched();

        // Recursion it self and analyse the origin element status.
        dealInnerClassList(mOriginTypeElement);

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

        if (mLinkedListStack.size() == 0) {
            builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get("chenanze.com.prodmapper", "ValueBinder"), mTargetClassName, mOriginClassName));
        } else {
            ClassName list = ClassName.get("java.util", "List");
            ClassName arrayListClassName = ClassName.get("java.util", "ArrayList");
            mTargetArrayListClassName = ParameterizedTypeName.get(list, mTargetClassName);
            builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get("chenanze.com.prodmapper", "ValueBinder"), mTargetArrayListClassName, mLinkedListStack.peek().getVariableType()));
        }

//        checkIsConstructorParameterMatched();

        if (mLinkedListStack.size() == 0) {
            createStaticTransformMethod(builder);
            createTransformIntoMethod(builder);
        } else {
            createStaticTransformListMethod(builder);
            createTransformIntoListMethod(builder);
        }
        return JavaFile.builder(PACKAGE_NAME, builder.build())
                .addFileComment("Generated code from ProdMapper. Do not modify!")
                .build();
    }

    /**
     * Recursion it self and analyse the origin element status.
     *
     * @param originTypeElement
     */
    private void dealInnerClassList(TypeElement originTypeElement) {
        Element parentElement = originTypeElement.getEnclosingElement();
        // If originTypeElement is top element in *.java file.
        // Stop call of recursion.
        // Push the top element to stack.
        if (parentElement.toString().equals(mPackageName)) {
//            List<VariableElement> parentVariableElements = ElementFilter.fieldsIn(parentElement.getEnclosedElements());
            mLinkedListStack.push(new Node(getVariableName(originTypeElement), getTypeName(originTypeElement), true));
            return;
        }

        TypeElement parentTypeElement = (TypeElement) originTypeElement.getEnclosingElement();
        List<VariableElement> parentVariableElements = ElementFilter.fieldsIn(parentElement.getEnclosedElements());
        List<ExecutableElement> parentMethodElements = ElementFilter.methodsIn(parentElement.getEnclosedElements());

        boolean isMatched = false;

        String templete = "^java.util.List<(.*)>$";
        Pattern compile = Pattern.compile(templete);

        // Find the class type java.util.List<(.*)> in parent class to match the origin class.
        for (VariableElement parentVariableElement : parentVariableElements) {
            String parentVariableName = parentVariableElement.getSimpleName().toString();
            String parentVariableTypeName = parentVariableElement.asType().toString();
            String originTypeName = originTypeElement.asType().toString();
            String originClassName = originTypeElement.getSimpleName().toString();
            Log.printLog("Type: " + parentVariableTypeName + " Name: " + parentVariableName);
            Log.printLog("Origin Type: " + originTypeName + "Origin Name: " + originClassName);

            Matcher matcher = compile.matcher(parentVariableTypeName);
            if (!matcher.matches()) {
                Log.printLog("is not match");
                continue;
            }
            String parameterizedTypeName = matcher.group(1);
            Log.printLog("group1: " + parameterizedTypeName);
            // To find it.
            if (originTypeName.equals(parameterizedTypeName)) {
                // Check if have VariableElement have the related public Get Method.
                boolean isGetMethodMatched = false;
//                boolean isGetMethodModifierPrivate = false;
                for (ExecutableElement parentMethodElement : parentMethodElements) {
                    // Check if originElement can find a VariableElement to match it in it's super class.
                    if (getGetAttributeMethod(parentVariableName).equals(parentMethodElement.getSimpleName() + "()")) {
                        Log.printLog("------> equals ----->");
                        // Check if the modifiers of VariableElement is not private.
                        if (!checkElementModifiers(parentMethodElement, Modifier.PRIVATE)) {
                            // If is private.
//                            isGetMethodModifierPrivate = true;
                        } else {// If is not private and find it.
                            // Add the getMethod name and return typeName to stack.
                            mLinkedListStack.push(new Node(getGetAttributeMethod(parentVariableName), getTypeName(originTypeElement)));
                            mOriginFiledsInSpuerStatesMap.put(parentVariableName, GET_METHOD);
                            isGetMethodMatched = true;
                            break;
                        }
                    }
                }
                // If the modifier of get method is private or the name of get method match to VariableElement.
                // Then check if the modifiers of VariableElement is public
                if (!isGetMethodMatched) {
                    Log.printLog("------>public-------->");
                    checkElementModifiers(parentVariableElement, Modifier.PUBLIC);
                    Log.printLog("parentVariableName: " + parentVariableName);
//                    mLinkedListStack.push(new Node(parentVariableName, getTypeName(parentVariableElement)));
                    mOriginFiledsInSpuerStatesMap.put(parentVariableName, FILED);
                    mLinkedListStack.push(new Node(parentVariableName, getTypeName(originTypeElement)));
                }
                isMatched = true;
                break;
            }
        }
        if (!isMatched) {
            String errorMessage = "Can not find a fields type java.util.List<("
                    + originTypeElement.asType().toString()
                    + ")> to match the origin class "
                    + originTypeElement.asType().toString()
                    + " in class: " + parentElement.asType().toString();
            Log.printError(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        dealInnerClassList(parentTypeElement);
    }

    private boolean checkElementModifiers(Element element, Modifier modifier) {
        switch (modifier) {
            case PRIVATE:
                if (element.getModifiers().contains(modifier)) {
                    String errorMessage = "Please make true the access of the element of "
                            + element.getSimpleName() + " in "
                            + getClassName(mOriginTypeElement) + " is not private ";
                    Log.printWarning(errorMessage);
                    return false;
//                    throw new RuntimeException(errorMessage);
                }
                break;
            default:
                if (!element.getModifiers().contains(modifier)) {
                    String errorMessage = "Please make true the access of the element of "
                            + element.getSimpleName() + " in "
                            + getClassName(mOriginTypeElement) + " is public ";
                    Log.printError(errorMessage);
                    throw new RuntimeException(errorMessage);
                }
                break;
        }
        return true;
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
                checkElementModifiers(orginFiledElement, Modifier.PUBLIC);
//                if (!orginFiledElement.getModifiers().contains(Modifier.PUBLIC)) {
//                    String errorMessage = "Please make true the type of access of the field of " + orginFiledElement.getSimpleName() + " in " + getClassName(mOriginTypeElement) + " is public ";
//                    Log.printError(errorMessage);
//                    throw new RuntimeException(errorMessage);
//                }
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
                    parameters.append(originObject + "." + variableElement.getSimpleName() + ",\n");
                    break;
                case GET_METHOD:
                    parameters.append(originObject + "." + getGetAttributeMethod(getMethodParameter(variableElement)) + ",\n");
                    break;
            }
        }
        parameters.delete(parameters.length() - 2, parameters.length());
        methodBuilder.addStatement("return new $T(" + parameters + ")", mTargetClassName);
        builder.addMethod(methodBuilder.build());
    }

    private void createTransformIntoListMethod(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("transformInto");
        createTransformListMethod(builder, methodBuilder);
    }

    private void createStaticTransformListMethod(TypeSpec.Builder builder) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("transforms");
        methodBuilder.addModifiers(Modifier.STATIC);
        createTransformListMethod(builder, methodBuilder);
    }

    private void createTransformListMethod(TypeSpec.Builder builder, MethodSpec.Builder methodBuilder) {

//        String originObject = getVariableName(mOriginTypeElement);

        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayListClassName = ClassName.get("java.util", "ArrayList");
        TypeName targetArrayListClassName = ParameterizedTypeName.get(list, mTargetClassName);

        Node topNode = mLinkedListStack.peek();
        methodBuilder.addModifiers(Modifier.PUBLIC)
                .addParameter(topNode.getVariableType(), topNode.getVariableName())
                .returns(mTargetArrayListClassName);
        methodBuilder.addStatement("$T $L = new $T<>()", mTargetArrayListClassName, getVariableName(mTargetTypeElement), arrayListClassName);
        methodBuilder.beginControlFlow("if($L != null)", topNode.getVariableName());
        Log.printLog("mLinkedListStack.size: " + mLinkedListStack.size());
        ListIterator<Node> nodeListIterator = mLinkedListStack.listIterator();
        String preNodeVariableName = topNode.getVariableName();
        while (nodeListIterator.hasNext()) {
            Node node = nodeListIterator.next();
            Log.printLog("node: " + node.toString());
            if (node.isTopNode()) continue;
            String variableName = getVariableNameFromTypeName(node.getVariableType());
            Log.printLog("node: " + node.toString());
            Log.printLog("variableName--->: " + variableName);
            methodBuilder.beginControlFlow("for($T $L : $L )", node.getVariableType(), variableName, preNodeVariableName + "." + node.getVariableName());

            // If is the last node.
            if (!nodeListIterator.hasNext()) {
                StringBuilder parameters = new StringBuilder();
                for (VariableElement variableElement : mConstructorElement.getParameters()) {
                    ConstructorParameterState constructorParameterState = mConsturctorParameterStatesMap.get(variableElement.getSimpleName().toString());
                    switch (constructorParameterState) {
                        case FILED:
                            parameters.append(variableName + "." + variableElement.getSimpleName() + ",\n");
                            break;
                        case GET_METHOD:
                            parameters.append(variableName + "." + getGetAttributeMethod(getMethodParameter(variableElement)) + ",\n");
                            break;
                    }
                }
                parameters.delete(parameters.length() - 2, parameters.length());
                methodBuilder.addStatement("$L.add(new $T(" + parameters + "))", getVariableName(mTargetTypeElement), mTargetClassName);
            }

            preNodeVariableName = variableName;
        }

        for (int i = 0; i < mLinkedListStack.size() - 1; i++) {
            methodBuilder.endControlFlow();
        }
        methodBuilder.endControlFlow();
        methodBuilder.addStatement("return $L", getVariableName(mTargetTypeElement));
        builder.addMethod(methodBuilder.build());
    }

    private String getVariableNameFromTypeName(TypeName variableType) {
        Log.printLog("variableType: " + variableType);
        String[] split = variableType.toString().split("\\.");
//        StringBuilder stringBuilder = new StringBuilder(split[split.length - 1]);
//        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        return lowCaseFirstChar(stringBuilder.toString());
        return lowCaseFirstChar(split[split.length - 1]);
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

    public String getPackageName(Element typeElement) {
        return mElementsUtils.getPackageOf(typeElement).toString();
    }

    public String getClassName(TypeElement typeElement) {
        return typeElement.getSimpleName().toString();
    }

    public TypeName getTypeName(Element element) {
        return ClassName.get(element.asType());
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

    public String lowCaseFirstChar(String content) {
        StringBuilder stringBuilder = new StringBuilder(content);
        stringBuilder.replace(0, 1, String.valueOf(stringBuilder.charAt(0)).toLowerCase());
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

    class Node {
        private String mVariableName;
        private TypeName mVariableType;
        private boolean mIsTopNode = false;

        public Node(String mVariableName, TypeName mVariableType) {
            this.mVariableName = mVariableName;
            this.mVariableType = mVariableType;
        }

        public Node(String mVariableName, TypeName mVariableType, boolean mIsTopNode) {
            this.mVariableName = mVariableName;
            this.mVariableType = mVariableType;
            this.mIsTopNode = mIsTopNode;
        }

        public String getVariableName() {
            return mVariableName;
        }

        public void setVariableName(String mVariableName) {
            this.mVariableName = mVariableName;
        }

        public TypeName getVariableType() {
            return mVariableType;
        }

        public void setVariableType(TypeName mVariableType) {
            this.mVariableType = mVariableType;
        }

        public boolean isTopNode() {
            return mIsTopNode;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "mVariableName='" + mVariableName + '\'' +
                    ", mVariableType=" + mVariableType +
                    ", mIsTopNode=" + mIsTopNode +
                    '}';
        }
    }
}
