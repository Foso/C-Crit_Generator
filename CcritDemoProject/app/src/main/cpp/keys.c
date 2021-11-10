#include <jni.h>
#include <string.h>
  

char* mod(char* input, int length, char* mod) {
    for (int i = 0; i < length-1; i++) input[i] = input[i] ^ mod[i % strlen(mod)];
    return input;
}
JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_ccritdemoproject_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {
   char enc[10] = {7, 27, 2, 58, 11, 4, 52, 23, 27, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 10, "stringFromJNI"));
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_ccritdemoproject_MainActivity_secondSe2cretString(JNIEnv *env, jobject instance) {
   char enc[11] = {7, 10, 19, 60, 11, 7, 33, 0, 70, 81, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 11, "secondSe2cretString"));
}

