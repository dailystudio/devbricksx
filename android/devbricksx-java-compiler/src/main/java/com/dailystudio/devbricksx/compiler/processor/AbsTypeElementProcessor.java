package com.dailystudio.devbricksx.compiler.processor;

import com.dailystudio.devbricksx.compiler.utils.LogUtils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class AbsTypeElementProcessor {

    protected Types mTypes;
    protected Filer mFiler;
    protected Elements mElementUtils;
    protected Messager mMessager;

    public void attachToProcessEnvironment(ProcessingEnvironment processingEnv) {
        if (processingEnv == null) {
            return;
        }

        mTypes = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    public void detachFromProcessEnvironment() {
        mFiler = null;
        mElementUtils = null;
        mMessager = null;
    }

    protected void debug(String format, Object... args) {
        LogUtils.debug(mMessager, format, args);
    }

    protected void info(String format, Object... args) {
        LogUtils.info(mMessager, format, args);
    }

    protected void error(String format, Object... args) {
        LogUtils.error(mMessager, format, args);
    }

    public void warn(String format, Object... args) {
        LogUtils.warn(mMessager, format, args);
    }

    protected String getPackageNameOfTypeElement(TypeElement typeElement) {
        if (typeElement == null) {
            return null;
        }

        return mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    protected String getTypeNameOfTypeElement(TypeElement typeElement) {
        if (typeElement == null) {
            return null;
        }

        return typeElement.getSimpleName().toString();
    }

}
