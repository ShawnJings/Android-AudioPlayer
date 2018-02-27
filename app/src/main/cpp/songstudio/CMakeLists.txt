#
# Copyright (C) 2016 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Build a static lib and copy it into distribution place for independent app to use
# we could not delete the bin directory is because android studio would error out
# when it checks target's existance. Only distribution place is used as import to app

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)

set(lib_src_DIR ${CMAKE_CURRENT_SOURCE_DIR})

include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/ffmpeg/include
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/libcommon)

add_library(
            fdk-aac
            STATIC
            IMPORTED
            )

set_target_properties(
            fdk-aac
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libfdk-aac.a
            )

add_library(
            avfilter
            STATIC
            IMPORTED
            )

set_target_properties(
            avfilter
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libavfilter.a
            )

add_library(
            vo-aacenc
            STATIC
            IMPORTED
            )

set_target_properties(
            vo-aacenc
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libvo-aacenc.a
            )

add_library(
            avformat
            STATIC
            IMPORTED
            )

set_target_properties(
            avformat
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libavformat.a
            )

add_library(
            avcodec
            STATIC
            IMPORTED
            )

set_target_properties(
            avcodec
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libavcodec.a
            )

add_library(
            avutil
            STATIC
            IMPORTED
            )

set_target_properties(
            avutil
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libavutil.a
            )

add_library(
            swscale
            STATIC
            IMPORTED
            )

set_target_properties(
            swscale
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libswscale.a
            )

add_library(
            swresample
            STATIC
            IMPORTED
            )

set_target_properties(
            swresample
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libswresample.a
            )

add_library(
            postproc
            STATIC
            IMPORTED
            )

set_target_properties(
            postproc
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libpostproc.a
            )

add_library(
            x264
            STATIC
            IMPORTED
            )

set_target_properties(
            x264
            PROPERTIES IMPORTED_LOCATION
            ${CMAKE_SOURCE_DIR}/src/main/cpp/songstudio/3rdparty/prebuilt/${ANDROID_ABI}/libx264.a
            )

add_subdirectory(${lib_src_DIR}/libcommon)
add_subdirectory(${lib_src_DIR}/libmusicdecoder)
add_subdirectory(${lib_src_DIR}/libmedia)

add_library(libsongstudio SHARED SoundTrackController.cpp MusicDecoder.cpp)

target_link_libraries(libsongstudio

            libcommontool
            libmusicdecoder
            libmedia

            avfilter
            avformat
            avcodec
            fdk-aac
            vo-aacenc
            avutil
            swscale
            swresample
            postproc
            x264

            OpenSLES
            z
            android
            log)