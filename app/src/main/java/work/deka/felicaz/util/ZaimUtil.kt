package work.deka.felicaz.util

import android.content.Context
import com.google.api.client.auth.oauth.OAuthCredentialsResponse
import work.deka.felicaz.R
import work.deka.zaim.Zaim

private const val TOKEN = "token"
private const val TOKEN_SECRET = "token_secret"
private const val ZAIM_CREDENTIALS = "zaim_credentials"

fun zaim(context: Context) = Zaim(
    Zaim.Configuration(
        context.getString(R.string.consumer_key),
        context.getString(R.string.consumer_secret),
        context.getString(R.string.callback_url)
    ),
    loadCredentials(context)
)

fun saveCredentials(context: Context, credentials: OAuthCredentialsResponse) {
    val preferences = context.getSharedPreferences(ZAIM_CREDENTIALS, Context.MODE_PRIVATE)
    preferences.edit()
        .putString(TOKEN, credentials.token)
        .putString(TOKEN_SECRET, credentials.tokenSecret)
        .apply()
}

fun loadCredentials(context: Context): OAuthCredentialsResponse {
    val preferences = context.getSharedPreferences(ZAIM_CREDENTIALS, Context.MODE_PRIVATE)
    return OAuthCredentialsResponse().apply {
        token = preferences.getString(TOKEN, "")
        tokenSecret = preferences.getString(TOKEN_SECRET, "")
    }
}

fun clearCredentials(context: Context) {
    val preferences = context.getSharedPreferences(ZAIM_CREDENTIALS, Context.MODE_PRIVATE)
    preferences.edit().remove(TOKEN).remove(TOKEN_SECRET).apply()
}