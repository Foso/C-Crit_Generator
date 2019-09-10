package de.jensklingenberg

import de.jensklingenberg.ccrit.NativeSecret


data class User(
    val name: String,
    val email: String,
    val test: Int
) {

    @NativeSecret("topSecret")
    external fun getSecretString()

    @NativeSecret("topServe32dss")
    fun getSecondSecret() {

    }
}
