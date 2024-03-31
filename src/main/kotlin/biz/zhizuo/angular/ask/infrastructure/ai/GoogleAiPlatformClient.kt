package biz.zhizuo.angular.ask.infrastructure.ai

import com.google.auth.oauth2.GoogleCredentials
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.io.FileInputStream
import java.time.Duration

class GoogleAiPlatformClient() {
    data class Request(val instances: List<Instance>, val parameters: Parameters) {
        data class Instance(val content: String)
        data class Parameters(val autoTruncate: Boolean = false)
    }

    data class Response(val predictions: List<Prediction>, val metadata: Metadata) {
        data class Prediction(val embeddings: Embedding)

        data class Embedding(val statistics: Statistic, val values: List<Double>)

        data class Statistic(val token_count: Int, val truncated: Boolean)
        data class Metadata(val billableCharacterCount: Int)
    }

    fun predicate(request: Request, model: String): Response {
        val token = getAccessToken()
        val client = WebClient.builder()
            .baseUrl("https://us-central1-aiplatform.googleapis.com/v1/projects/ralph-gde/locations/us-central1/publishers/google/models/${model}:predict")
            .defaultHeader(
                "Authorization",
                "Bearer $token"
            )
            .codecs { it.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) }
            .build()

        val result = client.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(Response::class.java)
            .timeout(Duration.ofMinutes(10))
            .block()

        return result!!
    }

    private fun getAccessToken(): String {
        val credentials =
            GoogleCredentials.fromStream(FileInputStream("./secrets/application_default_credentials.json"))

        credentials.refreshIfExpired()
        // 获取访问令牌
        return credentials.accessToken.tokenValue
    }
}
