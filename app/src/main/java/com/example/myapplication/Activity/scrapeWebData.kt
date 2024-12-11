import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

suspend fun scrapeWebData(url: String): List<String> {
    return withContext(Dispatchers.IO) {
        try {
            // Use OkHttp to fetch the web page
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val htmlContent = response.body?.string()

            // Parse the HTML using Jsoup
            val document = Jsoup.parse(htmlContent)
            val items = mutableListOf<String>()

            // Select elements based on CSS selectors
            val elements = document.select(".item-class") // Replace with your CSS selector
            for (element: Element in elements) {
                items.add(element.text())
            }

            items
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
