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

    val packageNameCFunc = """
        |
        |static jstring get_package_name(JNIEnv *env, jobject jActivity) {
        |    jclass jActivity_class = env->GetObjectClass(jActivity);
        |
        |    jmethodID jMethod_id_pn = env->GetMethodID(jActivity_class, "getPackageName", "()Ljava/lang/String;");
        |    jstring package_name = (jstring) env->CallObjectMethod(jActivity, jMethod_id_pn);
        |
        |    __android_log_print(ANDROID_LOG_DEBUG, TAG, "package name: %s\n", package_name);
        |    printf("package name: %s\n", package_name);
        |    return package_name;
        |}
        |
        |
    """.trimMargin()

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
            stringBuilder.append(packageNameCFunc)

            annotatedFunctionsList.forEach { function ->
                val packageName = processingEnv.elementUtils.getPackageOf(function).toString()
                val functionNameWithPackageAndUnderScores =
                    (packageName + "_" + function.enclosingElement.simpleName + "_" + function.simpleName).replace(
                        ".",
                        "_"
                    )
                val secret = function.getAnnotation(NativeSecret::class.java).secret
                val encodedSecret = encode(secret, processingEnv.options["package"] ?: "package")
                stringBuilder.append(getCFunction(encodedSecret, functionNameWithPackageAndUnderScores))
            }

            val outputFilePath =
                processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.substringBefore("build") + processingEnv.options["ccrit"]

            File(outputFilePath).also { file ->
                file.parentFile.mkdirs()
            }.writeText(stringBuilder.toString())
        }
        return true
    }

    private fun encode(text: String, key: String): String {
        val result = java.lang.StringBuilder()
        for (c in text.indices) result.append((text[c].toInt() xor key[c % key.length].toInt()).toChar())
        return result.toString()
    }
}

