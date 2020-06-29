# Face transformation
Face transformation / FaceCrop aims to help you easily transform image with face centered on Android ImageView when using Glide.

[![](https://jitpack.io/v/msomu/face-transformation.svg)](https://jitpack.io/#msomu/face-transformation)

## Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.msomu:face-transformation:latest.release.here'
}
```

## Features
- Automatically detects the face / faces in the image and center crops the image to fit the ImageView.
- If no faces are detected fallsback to CenterCrop

## Usage
There is a [sample](https://github.com/msomu/face-transformation/tree/master/app) provided which shows how to use the library, but for completeness, here is all that is required to get FaceCrop working:
```java
Glide.with(this).load(url)
.transform(FaceCrop()) // add this line
.into(imageView)
```
That's it!

## Face detection
This library uses ML Kit https://g.co/mlkit on-device machine learning.

License
--------

    Copyright 2020 Somasudnaram Mahesh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
