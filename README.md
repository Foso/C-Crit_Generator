<h1 align="center">C-Crit Generator</h1>

[![jCenter](https://img.shields.io/badge/Kotlin-1.3.50-green.svg
)](https://github.com/Foso/C-Crit_Generator/blob/master/LICENSE)[![jCenter](https://img.shields.io/badge/Apache-2.0-green.svg)](https://github.com/Foso/C-Crit_Generator/blob/master/LICENSE)
[![All Contribtors](https://img.shields.io/badge/Maven-Central-download.svg?style=flat-square)](https://mvnrepository.com/artifact/de.jensklingenberg/ccrit-generator)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![All Contributors](https://img.shields.io/badge/all_contributors-1-range.svg?style=flat-square)](#contributors)
  <a href="https://twitter.com/intent/tweet?text=Hey, check out C-Crit_Generator https://github.com/Foso/C-Crit_Generator via @jklingenberg_ #Kotlin 
"><img src="https://img.shields.io/twitter/url/https/github.com/angular-medellin/meetup.svg?style=social" alt="Tweet"></a>



## Introduction 🙋‍♂️ 🙋‍
You want to store your critical secret keys in your Android App in a C file, but you don't want to deal with C?
Let C-Crit Generator generate your C Keys file.



### Show some :heart: and star the repo to support the project

[![GitHub stars](https://img.shields.io/github/stars/Foso/C-Crit_Generator.svg?style=social&label=Star)](https://github.com/Foso/C-Crit_Generator) [![GitHub forks](https://img.shields.io/github/forks/Foso/C-Crit_Generator.svg?style=social&label=Fork)](https://github.com/Foso/C-Crit_Generator/fork) [![GitHub watchers](https://img.shields.io/github/watchers/Foso/C-Crit_Generator.svg?style=social&label=Watch)](https://github.com/Foso/C-Crit_Generator) [![Twitter Follow](https://img.shields.io/twitter/follow/jklingenberg_.svg?style=social)](https://twitter.com/jklingenberg_)


## Usage

Add the dependency from MavenCentral to your top level build.gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
        classpath "de.jensklingenberg:ccrit-generator:1.2.0"
}
```
Add the dependency from MavenCentral to your app build.gradle
```groovy
dependencies {
    implementation "de.jensklingenberg:ccrit-generator:1.2.0"
    kapt "de.jensklingenberg:ccrit-generator:1.2.0"
}
```
Configure the processor in your build.gradle
Add the javaCompileOptions inside <strong> android{} </strong>, like here https://github.com/Foso/C-Crit_Generator/blob/master/CcritDemoProject/app/build.gradle

```kotlin
 javaCompileOptions {
            annotationProcessorOptions {
                includeCompileClasspath = true
            }
        }
```

Add the kapt options:

```kotlin

kapt {
    arguments {
        arg("ccrit", "src/main/cpp/keys.c")
    }
}
```
Change the "src/main/cpp/keys.c" to the filepath where your file should be generated

You still need to configure your android project to use keys from native code (https://medium.com/@abhi007tyagi/storing-api-keys-using-android-ndk-6abb0adcadad)

Annotate your external functions with @NativeSecret() and the key you want to put in your C File

```kotlin
@NativeSecret("mySecretKey12")
external fun getPassword(): String
```

Now the processor will generated the C-Code for all your annotated functions into the file you set in the kapt-arguments
```kotlin
#include <jni.h>
#include <string.h>
  
JNIEXPORT jstring JNICALL
Java_de_jensklingenberg_myapplication_KeysModule_getPassword(JNIEnv *env, jobject instance) {

return (*env)->  NewStringUTF(env, "mySecretKey12");
}

```
## ✍️ Feedback

Feel free to send feedback on [Twitter](https://twitter.com/jklingenberg_) or [file an issue](https://github.com/foso/C-Crit_Generator/issues/new). Feature requests are always welcome.

## 👷 Development Project Structure
 	
* <kbd>processor</kbd> - the module that contains the code for the annotation processor
* <kbd>example</kbd> - A Kotlin Jvm project which uses the processor
* <kbd>CcritDemoProject</kbd> - A Android Demo Project that uses native code and the Ccrit Generator



## 📜 License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE.md](https://github.com/Foso/C-Crit_Generator/blob/master/LICENSE) file for details

-------

    Copyright 2019 Jens Klingenberg

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


