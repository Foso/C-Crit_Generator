#include <jni.h>
#include <string.h>
  

char* mod(char* input, int length, char* mod) {
    for (int i = 0; i < length-1; i++) input[i] = input[i] ^ mod[i % strlen(mod)];
    return input;
}
JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecretString(JNIEnv *env, jobject instance) {
   char enc[10] = {19, 10, 4, 0, 0, 0, 0, 0, 0, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 10, "getSecretString"));
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User_getSecondSecret(JNIEnv *env, jobject instance) {
   char enc[14] = {19, 10, 4, 0, 0, 17, 25, 11, 87, 97, 1, 16, 1, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 14, "getSecondSecret"));
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecretString(JNIEnv *env, jobject instance) {
   char enc[9] = {85, 85, 7, 97, 29, 27, 25, 87, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 9, "getSecretString"));
}

JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_User2_getSecondSecret(JNIEnv *env, jobject instance) {
   char enc[14] = {19, 10, 4, 0, 0, 17, 25, 11, 87, 97, 1, 16, 1, '\0'};
   return (*env)->  NewStringUTF(env, mod(enc, 14, "getSecondSecret"));
}

