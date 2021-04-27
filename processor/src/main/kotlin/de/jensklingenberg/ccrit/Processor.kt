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

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
        mutableSetOf(NativeSecret::class.java.name)

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    private val header = """
                    |#include <jni.h>
                    |#include <string.h>
                    |  
                    |""".trimMargin()

    private val modifyCFunc = """
        |
        |char* mod(char* input, int length, char* mod) {
        |    for (int i = 0; i < length-1; i++) input[i] = input[i] ^ mod[i % strlen(mod)];
        |    return input;
        |}
        |
    """.trimMargin()

    private fun getCFunction(
        secret: String,
        length: Int,
        functionName: String,
        simpleName: String
    ): String {
        return """
                    |JNIEXPORT jstring JNICALL
                    |Java_$functionName(JNIEnv *env, jobject instance) {
                    |   $secret
                    |   return (*env)->  NewStringUTF(env, mod(enc, ${length + 1}, "$simpleName"));
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
            stringBuilder.append(modifyCFunc)

            annotatedFunctionsList.forEach { function ->
                val packageName = processingEnv.elementUtils.getPackageOf(function).toString()
                val functionNameWithPackageAndUnderScores =
                    (packageName + "_" + function.enclosingElement.simpleName + "_" + function.simpleName).replace(
                        ".",
                        "_"
                    )
                val secret = encode(
                    function.getAnnotation(NativeSecret::class.java).secret,
                    function.simpleName.toString()
                )
                stringBuilder.append(
                    getCFunction(
                        toCharArray(secret),
                        secret.length,
                        functionNameWithPackageAndUnderScores,
                        function.simpleName.toString()
                    )
                )
            }

            val outputFilePath =
                processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]?.substringBefore("build") + processingEnv.options["ccrit"]

            File(outputFilePath).also { file ->
                file.parentFile.mkdirs()
            }.writeText(stringBuilder.toString())
        }
        return true
    }

    private fun toCharArray(input: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("char enc[${input.length + 1}] = {")
        for (i in input.indices) {
            val char = input[i]
            stringBuilder.append(char.toInt())
            if (i < input.length - 1) stringBuilder.append(", ")
        }
        stringBuilder.append(", '\\0'};")
        return stringBuilder.toString()
    }

    private fun encode(text: String, key: String): String {
        val result = java.lang.StringBuilder()
        for (c in text.indices) result.append((text[c].toInt() xor key[c % key.length].toInt()).toChar())
        return result.toString()
    }
}

