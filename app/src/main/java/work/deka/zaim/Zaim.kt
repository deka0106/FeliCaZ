package work.deka.zaim

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl
import com.google.api.client.auth.oauth.OAuthCredentialsResponse
import com.google.api.client.auth.oauth.OAuthHmacSigner
import com.google.api.client.auth.oauth.OAuthParameters
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.UrlEncodedParser
import com.google.api.client.http.javanet.NetHttpTransport
import work.deka.zaim.exception.ZaimException
import work.deka.zaim.home.user.GetUserVerifyRequest
import java.io.IOException

class Zaim(
    private val configuration: Configuration,
    val credentials: OAuthCredentialsResponse = OAuthCredentialsResponse()
) {
    private val temporaryCredentials: OAuthCredentialsResponse = OAuthCredentialsResponse()

    private val httpRequestFactory: HttpRequestFactory by lazy {
        if (!isAuthorized()) throw ZaimException("Not authorized.")
        val hmac = OAuthHmacSigner().apply {
            clientSharedSecret = configuration.consumerSecret
            tokenSharedSecret = credentials.tokenSecret
        }
        hmac.tokenSharedSecret = credentials.tokenSecret
        NetHttpTransport().createRequestFactory(OAuthParameters().apply {
            signer = hmac
            version = "1.0"
            consumerKey = configuration.consumerKey
            token = credentials.token
        })
    }

    fun request(): HttpRequestFactory = httpRequestFactory

    fun buildUrl(path: String): GenericUrl = GenericUrl("${configuration.baseUrl}$path")

    fun isAuthorized(): Boolean = !credentials.token.isNullOrBlank() && !credentials.tokenSecret.isNullOrBlank()

    fun getAuthorizeUrl(): String {
        updateTemporaryCredentials()
        return OAuthAuthorizeTemporaryTokenUrl(configuration.authorizeUrl).apply {
            temporaryToken = temporaryCredentials.token
        }.build()
    }

    fun authorize(code: String) {
        val response = NetHttpTransport().createRequestFactory(OAuthParameters().apply {
            signer = OAuthHmacSigner().apply {
                clientSharedSecret = configuration.consumerSecret
                tokenSharedSecret = temporaryCredentials.tokenSecret
            }
            version = "1.0"
            consumerKey = configuration.consumerKey
            token = temporaryCredentials.token
            verifier = code
        }).buildGetRequest(GenericUrl(configuration.accessTokenUrl)).execute()
        try {
            UrlEncodedParser.parse(response.parseAsString(), credentials)
        } catch (e: IOException) {
            throw ZaimException("Failed to authorize.", e)
        }
    }

    fun getUserVerify(): GetUserVerifyRequest = GetUserVerifyRequest(this)

    private fun updateTemporaryCredentials() {
        val response = NetHttpTransport().createRequestFactory(OAuthParameters().apply {
            signer = OAuthHmacSigner().apply { clientSharedSecret = configuration.consumerSecret }
            version = "1.0"
            consumerKey = configuration.consumerKey
            callback = configuration.callbackUrl
        }).buildGetRequest(GenericUrl(configuration.requestTokenUrl)).execute()
        UrlEncodedParser.parse(response.parseAsString(), temporaryCredentials)
    }

    data class Configuration(
        val consumerKey: String,
        val consumerSecret: String,
        val callbackUrl: String,
        val baseUrl: String = "https://api.zaim.net",
        val requestTokenUrl: String = "https://api.zaim.net/v2/auth/request",
        val authorizeUrl: String = "https://auth.zaim.net/users/auth",
        val accessTokenUrl: String = "https://api.zaim.net/v2/auth/access"
    )

}