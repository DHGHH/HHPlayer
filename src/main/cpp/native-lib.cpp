#include <string>
#include "Student.h"
#include "com_hh_go2019_MainActivity.h"

JNIEXPORT jstring JNICALL Java_com_hh_go2019_MainActivity_testAndroid(
        JNIEnv *env,
        jobject /* this */) {
    Student student("hbx, hello jni", 22);
    return env->NewStringUTF(student.getName().c_str());
}

/*返回Java字符串*/
JNIEXPORT jstring JNICALL Java_com_hh_go2019_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

/*返回Java字符串数组*/
JNIEXPORT jobjectArray JNICALL Java_com_hh_go2019_MainActivity_stringArrayFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    jobjectArray stringArrayTest = 0;
    jstring str;
    jsize length = 10;
    std::string value = "123";
    stringArrayTest = (jobjectArray) (env->NewObjectArray(length, env->FindClass("java/lang/String"), NULL));
    for (int i = 0; i < length; i++) {
        str = env->NewStringUTF(value.c_str());
        env->SetObjectArrayElement(stringArrayTest, i, str);
    }
    return stringArrayTest;
}
