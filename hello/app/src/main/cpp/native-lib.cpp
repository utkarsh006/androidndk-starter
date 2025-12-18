#include <jni.h>

// Simple native function that adds two integers.
extern "C" JNIEXPORT jint JNICALL
Java_com_example_hello_MainActivity_addTwoNumbers(
        JNIEnv * /* env */,
        jobject /* this */,
        jint a,
        jint b) {
    return a + b;
}