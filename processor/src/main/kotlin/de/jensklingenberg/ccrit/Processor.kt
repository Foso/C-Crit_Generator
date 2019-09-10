package de.jensklingenberg.ccrit

import com.google.auto.service.AutoService
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement


@AutoService(Processor::class)
class CcritProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private var annotatedFunctionsList: ArrayList<Element> = arrayListOf()

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(NativeSecret::class.java.name)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    val header = """
                    |#include <jni.h>
                    |#include <string.h>
                    |  
                    |""".trimMargin()

    fun getCFunction(secret: String, functionName: String): String {
        return """
                    |JNIEXPORT jstring JNICALL
                    |Java_$functionName(JNIEnv *env, jobject instance) {
                    |
                    |return (*env)->  NewStringUTF(env, "$secret");
                    |}
                    |
                    |""".trimMargin()
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(NativeSecret::class.java)
            .forEach {
                annotatedFunctionsList.add(it)
            }

        if (roundEnv.processingOver()) {

            val stringBuilder = StringBuilder()
            stringBuilder.append(header)

            annotatedFunctionsList.forEach { function ->
                val packageName = processingEnv.elementUtils.getPackageOf(function).toString()
                val functionNameWithPackageAndUnderScores =
                    (packageName + "_" + function.enclosingElement.simpleName + "_" + function.simpleName).replace(
                        ".",
                        "_"
                    )
                val secret = function.getAnnotation(NativeSecret::class.java).secret
                stringBuilder.append(getCFunction(secret, functionNameWithPackageAndUnderScores))
            }

            val outputFilePath =
                processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.substringBefore("build") + processingEnv.options["ccrit"]

            File(outputFilePath).also { file ->
                file.parentFile.mkdirs()
            }.writeText(stringBuilder.toString())
        }
        return true
    }
}

