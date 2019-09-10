#include <jni.h>
#include <string.h>
  
JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_ccritdemoproject_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "topSecret");
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_ccritdemoproject_MainActivity_secondSecretString(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "topSecret2");
}

