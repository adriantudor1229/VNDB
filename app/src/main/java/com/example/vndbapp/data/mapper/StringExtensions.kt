package com.example.vndbapp.data.mapper

fun String.stripBBCode(): String {
    return this
        // [FROM[url=...]] - remove the FROM wrapper but keep inner link text
        .replace(Regex("\\[FROM\\[url=[^]]*](.*?)\\[/url]]"), "$1")
        // [url=...]text[/url] - keep the link text
        .replace(Regex("\\[url=[^]]*](.*?)\\[/url]"), "$1")
        // strip remaining tags like [b], [i], [spoiler], etc.
        .replace(Regex("\\[/?[a-zA-Z][^]]*]"), "")
        .trim()
}
