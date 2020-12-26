package com.ahmedfathy.articles.util

//extension to convert when from statement to expression to have a compile time safety
val <T> T.exhaustive: T
    get() = this