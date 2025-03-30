package com.fluffy.global.web


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Auth(

    val required: Boolean = true
)
