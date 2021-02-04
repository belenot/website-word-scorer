package com.belenot.poc.service

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.jsoup.Jsoup
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.net.URL


class AppTest {

    val logger = LoggerFactory.getLogger(this::class.java)

    val domain = "lukoil.ru"
    val urlString = "https://$domain/"
    val websiteScrapper = ResourceWebsiteScrapper("/lukoil.html", urlString, domain)

    @Test fun `get page info from lukoil_ru`() {
        val websitePageInfo = websiteScrapper.scrapeInfo()
        val expectedLinkCount = 286 // If scrape page with javascript enabled will be more(319)
        assertThat("website links count", websitePageInfo.links.size, equalTo(expectedLinkCount))
    }
}

class ResourceWebsiteScrapper(val resourcePath: String, urlString: String, domain: String): AbstractWebsiteScrapper(urlString, domain) {
    override val content: String
        get() = javaClass.getResource(resourcePath)?.readText()?:""
}