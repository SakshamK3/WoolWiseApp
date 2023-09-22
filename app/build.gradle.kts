plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.woolapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.woolapp"
        minSdk = 19
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("META-INF/DEPENDENCIES")
        exclude ("**/com.google.type.TimeZoneOrBuilder.class")
    }

    configurations {
        all {
            exclude ("protobuf-java")
            resolutionStrategy {
                force ("com.google.api.grpc:proto-google-common-protos:1.18.0")
            }
        }
    }

}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-firestore-ktx:21.4.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.squareup.picasso:picasso:2.8")

    implementation("com.google.firebase:protolite-well-known-types:18.0.0") {
        exclude("proto-google-common-protos")
        exclude("protobuf-java")
    }

    implementation("com.google.cloud:google-cloud-dialogflow:2.1.0") {
        exclude("com.google.guava","listenable-future")
    }

    implementation("io.grpc:grpc-okhttp:1.52.1")
    implementation("com.google.api:gax:1.65.0")
    implementation("com.google.api.grpc:proto-google-common-protos:1.18.0") {
        exclude("protolite-well-known-types")
    }
}
