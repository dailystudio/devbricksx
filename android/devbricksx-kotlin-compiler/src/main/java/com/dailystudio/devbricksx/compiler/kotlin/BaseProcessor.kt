package com.dailystudio.devbricksx.compiler.kotlin

import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.lang.model.SourceVersion

open abstract class BaseProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    protected fun writeToFile(result: GeneratedResult) {
        val typeSpec = result.classBuilder.build()

        typeSpec.name?.let { name ->
            val fileBuilder = FileSpec.builder(
                    result.packageName,
                    name)

            val file = fileBuilder.addType(typeSpec).build()

            val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            file.writeTo(File(kaptKotlinGeneratedDir))
        }
    }

}