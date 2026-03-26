plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.findhobbies"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.findhobbies"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation(libs.recyclerview)
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.14")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation ("org.mindrot:jbcrypt:0.4")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}