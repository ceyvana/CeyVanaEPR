package com.example.network

import android.util.Log
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    suspend fun generateContent(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e("GeminiClient", "Gemini API Key is placeholder or missing!")
            return@withContext "API key is missing in AI Studio Secrets! Please configure GEMINI_API_KEY."
        }

        val jsonRequest = JSONObject().apply {
            val partsArray = JSONArray().put(JSONObject().put("text", prompt))
            val contentsArray = JSONArray().put(JSONObject().put("parts", partsArray))
            put("contents", contentsArray)
        }

        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        val url = "$API_URL?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    return@withContext "Error: HTTP ${response.code} $errBody"
                }
                val bodyString = response.body?.string() ?: return@withContext "Error: Empty response"
                val jsonResponse = JSONObject(bodyString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val contentObj = candidates.getJSONObject(0).optJSONObject("content")
                    if (contentObj != null) {
                        val parts = contentObj.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text response")
                        }
                    }
                }
                "Response translation error: $bodyString"
            }
        } catch (e: Exception) {
            "Network connection error: ${e.message}"
        }
    }
}
