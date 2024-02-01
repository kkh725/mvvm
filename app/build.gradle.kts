plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.test.mvvm_prac"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.mvvm_prac"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        dataBinding = true

    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.navercorp.nid:oauth-jdk8:5.9.0") // jdk 8

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    implementation ("com.kakao.sdk:v2-user:2.11.0" )// 카카오 로그인

    //레트로핏 추가
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.google.code.gson:gson:2.10")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Gson 컨버터 추가

    implementation ("com.google.code.gson:gson:2.10")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}