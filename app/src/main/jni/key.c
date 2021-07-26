//
// Created by HuyND on 7/9/2021.
//
#include <jni.h>
#include <stdbool.h>
#include <string.h>

bool getpkg(JNIEnv *env, jobject thiz, jobject context);
JNIEXPORT jstring JNICALL
Java_com_basecleanarchitechturedagger_data_mapper_CryptMapper_getKey(
    JNIEnv *env,
    jobject instance,
    jobject context){
    if (getpkg(env, instance, context)) {
        return (*env)-> NewStringUTF(env, "API_KEY");
    } else {
        return (*env)-> NewStringUTF(env, "API_KEY_FAKE");
    }
}

bool getpkg(JNIEnv *env, jobject thiz, jobject context) {
    const char *package = "com.example.basecleanarchitechturedagger";

    jclass android_content_Context = (*env)->GetObjectClass(env, context);
    jmethodID midGetPackageName = (*env)->GetMethodID(env, android_content_Context,
                                                      "getPackageName", "()Ljava/lang/String;");
    jstring packageName = (jstring)(*env)->CallObjectMethod(env, context, midGetPackageName);

    const char *aaa = (*env)->GetStringUTFChars(env, packageName, 0);
#ifdef DEBUG
    __android_log_write(ANDROID_LOG_DEBUG, LOG_TAG, aaa);
    if (strcmp(aaa, package) == 0) {
        __android_log_write(ANDROID_LOG_DEBUG, LOG_TAG, "same");
    } else {
        __android_log_write(ANDROID_LOG_DEBUG, LOG_TAG, "different");
    }
#endif
    return (strcmp(aaa, package) == 0);
}

