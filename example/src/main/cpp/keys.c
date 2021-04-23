#include <jni.h>
#include <string.h>
  

static jstring get_package_name(JNIEnv *env, jobject jActivity) {
    jclass jActivity_class = env->GetObjectClass(jActivity);

    jmethodID jMethod_id_pn = env->GetMethodID(jActivity_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name = (jstring) env->CallObjectMethod(jActivity, jMethod_id_pn);

    __android_log_print(ANDROID_LOG_DEBUG, TAG, "package name: %s\n", package_name);
    printf("package name: %s\n", package_name);
    return package_name;
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecretString(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "
^9 
");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecondSecretXXX(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "
^9 _[
");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecretString(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "VU]XY");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecondSecret(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "
^9 _[
");
}

