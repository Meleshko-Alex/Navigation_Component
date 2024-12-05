package com.meleha.navcomponent.model

class LoadDataException : Exception()

class DuplicateException(
    val duplicate: String,
) : Exception("The list can't contain duplicated items")