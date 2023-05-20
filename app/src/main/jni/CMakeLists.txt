cmake_minimum_required (VERSION 3.10.0)

project(trime-lib VERSION 3.0.0)

set(ANDROID_STL c++_shared)
set(CMAKE_CXX_STANDARD 14)

# general options
include_directories(
    PRIVATE "${CMAKE_CURRENT_SOURCE_DIR}/librime/include"
)
# workaround for librime/tools/rime_api_console.cc
# TODO: fix it in upstream
string(REPLACE "-Werror=format-security" "" CMAKE_CXX_FLAGS ${CMAKE_CXX_FLAGS})
string(REPLACE "-Wformat" "" CMAKE_CXX_FLAGS ${CMAKE_CXX_FLAGS})
add_definitions("-Wno-format")
# workaround end

file(GLOB RIME_JNI_SOURCES librime_jni/*.cc)
add_library(rime_jni SHARED ${RIME_JNI_SOURCES})
target_include_directories(rime_jni PRIVATE
        "${CMAKE_CURRENT_SOURCE_DIR}/include/librime"
        )
target_link_libraries(rime_jni
        ${CMAKE_CURRENT_LIST_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/librime.so
        ${CMAKE_CURRENT_LIST_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libopencc.a
        )

string(TIMESTAMP TODAY "%Y%m%d")
set(TRIME_VERSION "${TRIME_VERSION}-${TODAY}")

set(LIBRIME_VERSION "${LIBRIME_VERSION}-${ANDROID_ABI}")

target_compile_definitions(rime_jni PRIVATE
        CLASSNAME="com/osfans/trime/core/Rime"
        TRIME_VERSION="${TRIME_VERSION}"
        OPENCC_VERSION="${OPENCC_VERSION}"
        LIBRIME_VERSION="${LIBRIME_VERSION}"
        )
