package de.jensklingenberg

import de.jensklingenberg.ccrit.NativeSecret

data class User2(
    val name: String,
    val email: String,
    val test: Int
) {

    @NativeSecret("20s2xxk2")
    fun getSecretString() {}

    @NativeSecret("topServe32dss")
    fun getSecondSecret() {}
}
