plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
//    id("org.greenrobot.greendao")
}

android {
    namespace = "com.joaoandrade.passwordarchive"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.joaoandrade.passwordarchive"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }

//    greendao{
//        schemaVersion =  2
//        daoPackage = "com.joaoandrade.passwordarchive.Model"
//        targetGenDir = "src/main/java"
//    }

}



dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")//com.google.android.material:material:1.10.0
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")//androidx.navigation:navigation-fragment:2.7.5
    implementation("androidx.navigation:navigation-ui:2.7.7")//androidx.navigation:navigation-ui:2.7.5

//    implementation("androidx.navigation:navigation-fragment:2.4.2")
//    implementation("androidx.navigation:navigation-ui:2.4.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.clans:fab:1.6.4")
//    implementation ("com.github.Ferfalk:SimpleSearchView:0.2.0")

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-storage")

    implementation("androidx.biometric:biometric:1.1.0")

    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("org.greenrobot:greendao:3.3.0") // greendao

    implementation("com.squareup.retrofit2:retrofit:2.9.0")//retrofit
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")//converter do retrofit
    implementation("com.squareup.picasso:picasso:2.8")

}