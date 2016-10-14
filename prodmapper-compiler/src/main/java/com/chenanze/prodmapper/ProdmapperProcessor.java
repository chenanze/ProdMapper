package com.chenanze.prodmapper;

import com.chenanze.prodmapper.utils.Log;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static com.chenanze.prodmapper.utils.Log.printError;
import static com.chenanze.prodmapper.utils.Log.printLog;

/**
 * Created by duian on 2016/10/10.
 */
@AutoService(Processor.class)
public class ProdmapperProcessor extends AbstractProcessor {

    private Filer mFileUtils;
    private static Elements mElementUtils;
    private Messager mMessager;
    private Map<String, ProxyClass> mProxyMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        Log.init(mMessager);
        Log.setLogStatus(Log.LOG_STATUS.RELEASE);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<String>();
        annotationTypes.add(BindType.class.getCanonicalName());
        annotationTypes.add(Construction.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Log.printLog("process-------->");
        mProxyMap.clear();
        findAndParseTargets(roundEnv, mElementUtils);

        for (Map.Entry<String, ProxyClass> proxyClassEntry :
                mProxyMap.entrySet()) {
            ProxyClass proxyClass = proxyClassEntry.getValue();

            if (!proxyClass.checkIsProcessCorrect(proxyClassEntry.getKey()))
                continue;

            try {
                proxyClass.generateSourceCode().writeTo(mFileUtils);
            } catch (IOException e) {
                Log.printError("Unable to write view binder for type %s: %s" + proxyClass.getProxyClassName());
                e.printStackTrace();
            }
            printLog("Map Entry: " + proxyClass.toString());
        }

        return true;
    }

    private void findAndParseTargets(RoundEnvironment roundEnv, Elements elementsUtils) {
        // Process each @BindType element
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindType.class);

        ProxyClass.setElementsUtils(elementsUtils);

        for (Element element : elements) {
            //检查element类型
            Log.printLog("Test--------------->");
            TypeElement typeElement = (TypeElement) element;
            BindType annotation = typeElement.getAnnotation(BindType.class);
            TypeMirror typeMirror = getClassValue(annotation);
            String key = typeMirror.toString();
            ProxyClass proxyClass = mProxyMap.get(key);
            if (proxyClass != null) {
                String errorMessage = "the " + typeMirror.toString() + " had been bind to " + typeElement.getQualifiedName();
                printError(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            mProxyMap.put(key, new ProxyClass(typeElement, annotation.proxyClassName()));
        }

        // Process each @Construction element
        elements = roundEnv.getElementsAnnotatedWith(Construction.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String key = typeElement.getQualifiedName().toString();
            ProxyClass proxyClass = mProxyMap.get(key);
            if (proxyClass == null) {
                String errorMessage = "The construction of " + key + " had not been bind to any origin class.";
                Log.printWarning(errorMessage);
                continue;
            }
            proxyClass.setConstructorElement((ExecutableElement) element);
            proxyClass.setTargetTypeElement(typeElement);
        }
    }

    private static TypeMirror getClassValue(BindType annotation) {
        try {
            annotation.value(); // this should throw
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null; // can this ever happen ??
    }

}
