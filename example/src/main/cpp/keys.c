#include <jni.h>
#include <string.h>
  
JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecretString(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "topSecret");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecondSecret(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "topServe32dss");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecretString(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "20s2xxk2");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecondSecret(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "topServe32dss");
}

