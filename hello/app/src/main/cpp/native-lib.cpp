#include <jni.h>

// Native function that receives ARGB pixel data and returns it unchanged.
// Signature matches: external fun processImage(pixels: IntArray, width: Int, height: Int): IntArray
extern "C" JNIEXPORT jintArray JNICALL
Java_com_example_hello_MainActivity_processImage(
        JNIEnv *env,
        jobject /* this */,
        jintArray pixels,
        jint /* width */,
        jint /* height */) {

    // Get pointer to input pixels
    jint *inPixels = env->GetIntArrayElements(pixels, nullptr);
    if (inPixels == nullptr) {
        return nullptr;
    }

    // Determine array length
    const jsize length = env->GetArrayLength(pixels);

    // Allocate new array for output
    jintArray outPixels = env->NewIntArray(length);
    if (outPixels == nullptr) {
        env->ReleaseIntArrayElements(pixels, inPixels, JNI_ABORT);
        return nullptr;
    }

    // For now just copy input to output (no processing).
    env->SetIntArrayRegion(outPixels, 0, length, inPixels);

    // Release input array without copying back (we already copied explicitly).
    env->ReleaseIntArrayElements(pixels, inPixels, JNI_ABORT);

    return outPixels;
}