package com.belenot.poc.service

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.Reader
import java.net.URL

interface WebsiteScrapper {
    fun scrapeInfo(): WebsitePageInfo
}

abstract class AbstractWebsiteScrapper(val urlString: String, val domain: String): WebsiteScrapper {

    private val logger = LoggerFactory.getLogger(this::class.java)
    abstract val content: String

    override fun scrapeInfo(): WebsitePageInfo {
        logger.info("Scrape Info for $urlString.")

        val document = Jsoup.parse(content)

        val links = document.select("a[href]")
            .filter { it.attr("href").contains(domain) }
            .map { it.attr("href") }

        return WebsitePageInfo(urlString, links)
            .also { logger.info("Scraped info for $urlString: $it") }
    }
}

class UrlWebsiteScrapper(urlString: String, domain: String): AbstractWebsiteScrapper(urlString, domain) {
    val ok = OkHttpClient()
    val requestBuilder = Request.Builder()
        .url(URL(urlString))
        .get()

    override val content: String
        get() = ok.newCall(requestBuilder.build()).execute().body?.string()?:""
}

data class WebsitePageInfo(
    val url: String,
    val links: List<String>
)

fun main(args: Array<String>) {

}