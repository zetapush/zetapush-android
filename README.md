# zetapush-android

Example of Android project that uses ZetaPush Android SDK

## Install

Add ZetaPush Nexus repository (`'http://nexus.zpush.io:8080/repository/public/'`) to your main Gradle file (`build.gradle` at root of your Android project):

```groovy
...
allprojects {
    repositories {
        jcenter()
        maven {
            url "http://nexus.zpush.io:8080/repository/public/"
        }
    }
}
...
```

Then add the ZetaPush Android dependency `'com.zetapush:android-client:[2.6.7, )'` to the application gradle file (`app/build.gradle`):

```groovy
...

dependencies {
    ...
    
    compile 'com.zetapush:android-client:[2.6.7, )'
}
...
```

The dependency uses a version range (`[2.6.7, )`). It means that the minimum version of ZetaPush SDK is 2.6.7 and there is no maximum version.

For a real project, it is better to fix the version (`2.6.7` instead of `[2.6.7, )`).


### Make it work with Android linter

Some dependencies embedded by ZetaPush SDK references classes that are not available in Android.
The classes that are using missing classes are never used by ZetaPush SDK. The classes are mainly
utilities provided by external libraries.

To disable lint checking, add `lintOptions` section in android configuration (in `app/build.gradle` file):

```groovy
android {
    ...

    lintOptions {
        lintConfig file("lint.xml")
    }
}
```

Creates a file named `lint.xml` in `app` directory with the following content:

```xml
<lint>
    <issue id="InvalidPackage">
        <ignore path="**/android-client*.jar"/>
    </issue>
</lint>
```

## Usage

TODO