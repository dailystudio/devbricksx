package com.dailystudio.devbricksx.compiler.processor.roomcompanion.typeelementprocessor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dailystudio.devbricksx.annotations.RoomCompanion;
import com.dailystudio.devbricksx.compiler.processor.AbsTypeElementProcessor;
import com.dailystudio.devbricksx.compiler.processor.Constants;
import com.dailystudio.devbricksx.compiler.processor.roomcompanion.AbsRoomCompanionTypeElementProcessor;
import com.dailystudio.devbricksx.compiler.processor.roomcompanion.GeneratedNames;
import com.dailystudio.devbricksx.compiler.utils.NameUtils;
import com.dailystudio.devbricksx.compiler.utils.TextUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class RoomCompanionClassProcessor extends AbsRoomCompanionTypeElementProcessor {

    @Override
    protected TypeSpec.Builder onProcess(TypeElement typeElement, String packageName, String typeName, RoundEnvironment roundEnv) {
        ClassName generatedClassName = ClassName
                .get(packageName, GeneratedNames.getRoomCompanionName(typeName));
        debug("generated class = [%s]", generatedClassName);

        RoomCompanion companionAnnotation = typeElement.getAnnotation(RoomCompanion.class);
        String primaryKey = null;
        if (companionAnnotation != null) {
            primaryKey = companionAnnotation.primaryKey();
        }

        if (TextUtils.isEmpty(primaryKey)) {
            return null;
        }

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Entity.class);

        List<? extends Element> subElements = typeElement.getEnclosedElements();

        VariableElement varElement;
        ExecutableElement constructorElement = null;
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        Set<String> fieldsOutsideConstructor = new HashSet<>();

        for (Element subElement: subElements) {
            if (subElement instanceof VariableElement) {
                debug("processing field: %s", subElement);

                varElement = (VariableElement) subElement;

                String varName = varElement.getSimpleName().toString();
                TypeMirror fieldType = varElement.asType();

                FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(TypeName.get(fieldType),
                        varName);

                fieldSpecBuilder.addAnnotation(AnnotationSpec.builder(ColumnInfo.class)
                        .addMember("name", "$S",
                                NameUtils.underscoreCaseName(varName))
                        .build()
                );

                if (primaryKey.equals(varName)) {
                    fieldSpecBuilder.addAnnotation(PrimaryKey.class);
                    fieldSpecBuilder.addAnnotation(NonNull.class);
                }

                fieldSpecs.add(fieldSpecBuilder.build());

                fields.add(varName);
                fieldsOutsideConstructor.add(varName);
            } else if (subElement instanceof ExecutableElement) {
                if (Constants.CONSTRUCTOR_NAME.equals(subElement.getSimpleName().toString())) {
                    debug("processing constructor: %s", subElement);
                    constructorElement = (ExecutableElement) subElement;
                }
            }
        }

        if (constructorElement == null) {
            warn("failed to access constructor of %s", typeElement);
            return null;
        }

        TypeName object = getObjectTypeName(packageName, typeName);

        MethodSpec.Builder methodToObjectBuilder = MethodSpec.methodBuilder("toObject")
                .addModifiers(Modifier.PUBLIC)
                .returns(object);

        StringBuilder constParamsBuilder = new StringBuilder();
        List<? extends VariableElement> constParams =
                constructorElement.getParameters();

        VariableElement param;
        String paramName;
        for (int i = 0; i < constParams.size(); i++) {
            param = constParams.get(i);

            paramName = param.getSimpleName().toString();

            debug("param: %s", param.getSimpleName());
            constParamsBuilder.append(paramName);
            if (i < constParams.size() - 1) {
                constParamsBuilder.append(", ");
            }

            fieldsOutsideConstructor.remove(paramName);
        }

        methodToObjectBuilder.addStatement("$T object = new $T($N)",
                object, object, constParamsBuilder.toString());

        for (String fieldName: fieldsOutsideConstructor) {
            methodToObjectBuilder.addStatement("object.$N = this.$N",
                    fieldName,
                    fieldName);
        }

        methodToObjectBuilder.addStatement("return object");

        MethodSpec.Builder methodToCompanionBuilder = MethodSpec.methodBuilder("fromObject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(object, NameUtils.lowerCamelCaseName(typeName))
                .addStatement("$T companionAnnotation = new $T()", generatedClassName, generatedClassName)
                .returns(generatedClassName);

        for (String fieldName: fields) {
            methodToCompanionBuilder.addStatement("companionAnnotation.$N = $N.$N",
                    fieldName,
                    NameUtils.lowerCamelCaseName(typeName),
                    fieldName);
        }

        methodToCompanionBuilder.addStatement("return companionAnnotation");

        for (FieldSpec fieldSpec: fieldSpecs) {
            classBuilder.addField(fieldSpec);
        }

        classBuilder.addMethod(methodToObjectBuilder.build());
        classBuilder.addMethod(methodToCompanionBuilder.build());

        return classBuilder;
    }

}
