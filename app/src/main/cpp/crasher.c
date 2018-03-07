#include <jni.h>

JNIEXPORT jstring JNICALL
Java_foo_crasher_Crasher_nativeCrash(JNIEnv *env, jobject instance) {
  int* p = 0;
  *p = 1;
  return (*env)->NewStringUTF(env, "Hello World.");
}
