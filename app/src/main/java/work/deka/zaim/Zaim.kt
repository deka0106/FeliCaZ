package work.deka.zaim

import oauth.signpost.OAuth
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider
import oauth.signpost.http.HttpParameters
import org.apache.http.HttpResponse
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.account.GetAccountRequest
import work.deka.zaim.home.category.GetCategoryRequest
import work.deka.zaim.home.genre.GetGenreRequest
import work.deka.zaim.home.money.GetMoneyRequest
import work.deka.zaim.home.money.income.PostMoneyIncomeRequest
import work.deka.zaim.home.money.payment.PostMoneyPaymentRequest
import work.deka.zaim.home.money.transfer.PostMoneyTransferRequest
import work.deka.zaim.home.user.GetUserVerifyRequest
import java.util.*

class Zaim(
    private val configuration: Configuration,
    val credentials: Credentials = Credentials()
) {
    private val consumer: CommonsHttpOAuthConsumer
        get() {
            if (!isAuthorized) throw ZaimException("Not authorized.")
            return CommonsHttpOAuthConsumer(configuration.consumerKey, configuration.consumerSecret).apply {
                setTokenWithSecret(credentials.token, credentials.tokenSecret)
            }
        }

    private val temporaryConsumer = CommonsHttpOAuthConsumer(configuration.consumerKey, configuration.consumerSecret)
    private val provider = CommonsHttpOAuthProvider(
        configuration.requestTokenUrl,
        configuration.accessTokenUrl,
        configuration.authorizeUrl
    )

    private val client get() = DefaultHttpClient()

    val isAuthorized get() = credentials.token.isNotBlank() && credentials.tokenSecret.isNotBlank()

    fun get(path: String, params: Map<String, Any?> = emptyMap()): HttpResponse {
        val url = "${configuration.baseUrl}$path?${
        params.entries.filterNot { it.value == null }.joinToString("&") {
            "${OAuth.percentEncode(it.key)}=${OAuth.percentEncode(it.value.toString())}"
        }}"
        val get = HttpGet(url)
        consumer.sign(get)
        return client.execute(get)
    }

    fun post(path: String, params: Map<String, Any?> = emptyMap()): HttpResponse {
        val url = "${configuration.baseUrl}$path"
        val post = HttpPost(url).apply {
            entity = UrlEncodedFormEntity(params.entries.filterNot { it.value == null }.map {
                BasicNameValuePair(it.key, it.value.toString())
            })
        }
        consumer.apply {
            setAdditionalParameters(HttpParameters().apply {
                for (param in params.entries) {
                    put(OAuth.percentEncode(param.key), OAuth.percentEncode(param.value.toString()))
                }
                put("realm", url)
            })
            sign(post)
        }
        return client.execute(post)
    }

    fun getAuthorizeUrl(): String = provider.retrieveRequestToken(temporaryConsumer, configuration.callbackUrl)

    fun authorize(verifier: String) {
        try {
            provider.retrieveAccessToken(temporaryConsumer, verifier)
            credentials.apply {
                token = temporaryConsumer.token
                tokenSecret = temporaryConsumer.tokenSecret
            }
        } catch (e: Exception) {
            throw ZaimException("Failed to authorize.", e)
        }
    }

    fun getUserVerify(): GetUserVerifyRequest = GetUserVerifyRequest(this)
    fun getMoney(): GetMoneyRequest = GetMoneyRequest(this)
    fun getCategory(): GetCategoryRequest = GetCategoryRequest(this)
    fun getGenre(): GetGenreRequest = GetGenreRequest(this)
    fun getAccount(): GetAccountRequest = GetAccountRequest(this)

    fun postMoneyPayment(
        mapping: Int, categoryId: Long, genreId: Long, amount: Int, date: Date
    ): PostMoneyPaymentRequest = PostMoneyPaymentRequest(this, mapping, categoryId, genreId, amount, date)

    fun postMoneyIncome(
        mapping: Int, categoryId: Long, amount: Int, date: Date
    ): PostMoneyIncomeRequest = PostMoneyIncomeRequest(this, mapping, categoryId, amount, date)

    fun postMoneyTransfer(
        mapping: Int, amount: Int, date: Date, fromAccountId: Long, toAccountId: Long
    ): PostMoneyTransferRequest = PostMoneyTransferRequest(this, mapping, amount, date, fromAccountId, toAccountId)

    data class Configuration(
        val consumerKey: String,
        val consumerSecret: String,
        val callbackUrl: String,
        val baseUrl: String = "https://api.zaim.net",
        val requestTokenUrl: String = "https://api.zaim.net/v2/auth/request",
        val authorizeUrl: String = "https://auth.zaim.net/users/auth",
        val accessTokenUrl: String = "https://api.zaim.net/v2/auth/access"
    )

    data class Credentials(
        var token: String = "",
        var tokenSecret: String = ""
    )

}