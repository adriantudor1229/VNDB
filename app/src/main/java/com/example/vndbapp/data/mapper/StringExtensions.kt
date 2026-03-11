package com.example.vndbapp.data.mapper

fun String.stripBBCode(): String {
    return this
        .replace(Regex("\\[FROM\\[url=[^]]*](.*?)\\[/url]]"), "$1")
        .replace(Regex("\\[url=[^]]*](.*?)\\[/url]"), "$1")
        .replace(Regex("\\[/?[a-zA-Z][^]]*]"), "")
        .trim()
}
