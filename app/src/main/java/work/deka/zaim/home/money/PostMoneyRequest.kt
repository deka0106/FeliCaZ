package work.deka.zaim.home.money

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Base64
import com.google.api.client.util.GenericData
import work.deka.zaim.Zaim
import work.deka.zaim.ZaimRequest
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.Mode
import java.io.IOException
import java.net.URLEncoder
import java.security.GeneralSecurityException
import java.security.MessageDigest


abstract class PostMoneyRequest(
    private val zaim: Zaim,
    private val mode: Mode
) : ZaimRequest<PostMoneyResponse> {

    override fun execute(): PostMoneyResponse {
        val url = zaim.buildUrl("/v2/home/money/$mode")
        val data = buildData()
        val response = try {
            zaim.request().buildPostRequest(url, JsonHttpContent(JacksonFactory(), data)).execute()
        } catch (e: HttpResponseException) {
            throw ZaimException("${e.message}")
        }
        if (response.isSuccessStatusCode) return jacksonObjectMapper().readValue(response.parseAsString())
        else throw ZaimException("Failed to request: $url by ${response.statusCode} ${response.statusMessage}")
    }

    protected abstract fun buildData(): GenericData

}