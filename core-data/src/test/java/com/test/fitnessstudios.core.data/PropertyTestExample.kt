package com.test.fitnessstudios.core.data
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import io.kotest.property.forAll

// https://github.com/Kotlin/kotlinx.coroutines/issues/2023
// https://github.com/Ivy-Apps/ivy-wallet/blob/3eaa9e17aa437f5434afaa73e8f8624d434ca4d6/buildSrc/src/main/java/com/ivy/buildsrc/dependencies.kt#L487
class PropertyTestExample : StringSpec({
    "length should return size of string" {
        "hello".length shouldBe 5
    }

    "startsWith should test for a prefix" {
        "world" should startWith("wor")
    }

    // Nice example for a property test
    "String size" {
        forAll<String, String> { a, b ->
            a.length + b.length == (a + b).length
        }
    }
})

// kotest-runner-junit5-test-jvm