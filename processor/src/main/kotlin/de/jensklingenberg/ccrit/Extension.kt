package de.jensklingenberg.ccrit

@Target(AnnotationTarget.FUNCTION)
annotation class NativeSecret(val secret: String)
