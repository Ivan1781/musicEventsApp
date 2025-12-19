package testUtils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

fun getElements(tag: String, html: String): Elements {
    val document: Document = Jsoup.parse(html)
    return document.getElementsByTag(tag)
}

fun getElementsByCss(css: String, html: String): Elements {
    val document: Document = Jsoup.parse(html)
    return document.select(css)
}