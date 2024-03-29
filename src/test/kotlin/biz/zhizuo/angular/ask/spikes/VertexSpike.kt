package biz.zhizuo.angular.ask.spikes

import biz.zhizuo.angular.ask.infrastructure.ai.GoogleAiPlatformConfiguration
import org.apache.commons.math3.linear.ArrayRealVector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.ai.autoconfigure.vertexai.gemini.VertexAiGeminiAutoConfiguration
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [VertexAiGeminiAutoConfiguration::class, RestClientAutoConfiguration::class, GoogleAiPlatformConfiguration::class])
class VertexSpike {
    @Autowired
    lateinit var embeddingClient: EmbeddingClient

    @Test
    fun testEmbedding() {
        assertThat(embeddingClient.embed("This is a test.")).isEqualTo(embeddingForEnglishAnswer)
        assertThat(embeddingClient.embed("这是一个测试。")).isEqualTo(embeddingForChineseAnswer)
        assertThat(embeddingClient.embed("What's this?")).isEqualTo(embeddingForEnglishQuestion)
        assertThat(embeddingClient.embed("这是什么？")).isEqualTo(embeddingForChineseQuestion)
        assertThat(embeddingClient.embed("Who are you?")).isEqualTo(embeddingForAnotherEnglishQuestion)
        assertThat(embeddingClient.embed("你是谁？")).isEqualTo(embeddingForAnotherChineseQuestion)
    }

    @Test
    @DisplayName("相同含义的中文和英文之间，内积应该比较大")
    fun testSimilarityBetweenEnglishAndChinese() {
        assertThat(embeddingForChineseAnswer.innerProduct(embeddingForEnglishAnswer)).isEqualTo(
            0.8933128758094787
        )
        assertThat(embeddingForChineseQuestion.innerProduct(embeddingForEnglishQuestion)).isEqualTo(
            0.8370542431178384
        )
        assertThat(embeddingForAnotherChineseQuestion.innerProduct(embeddingForAnotherEnglishQuestion)).isEqualTo(
            0.8681604081072648
        )
    }

    @Test
    @DisplayName("问题和正确答案之间，余弦距离应该比错误答案小")
    fun testSimilarityBetweenQuestionAndAnswer() {
        assertThat(embeddingForChineseQuestion.cosineDistance(embeddingForChineseAnswer)).isEqualTo(
            0.18484356810184377
        )
        assertThat(embeddingForEnglishQuestion.cosineDistance(embeddingForEnglishAnswer)).isEqualTo(
            0.23336899993539872
        )
        assertThat(embeddingForAnotherChineseQuestion.cosineDistance(embeddingForChineseAnswer)).isEqualTo(
            0.2446143364246075
        )
        assertThat(embeddingForAnotherEnglishQuestion.cosineDistance(embeddingForEnglishAnswer)).isEqualTo(
            0.307516820775269
        )
    }
}

fun List<Double>.cosineDistance(antherEmbedding: List<Double>): Double {
    val vectorForThis = ArrayRealVector(this.toDoubleArray())
    val vectorForAnother = ArrayRealVector(antherEmbedding.toDoubleArray())
    return 1 - vectorForThis.cosine(vectorForAnother)
}

fun List<Double>.innerProduct(antherEmbedding: List<Double>): Double {
    val vectorForThis = ArrayRealVector(this.toDoubleArray())
    val vectorForAnother = ArrayRealVector(antherEmbedding.toDoubleArray())
    return vectorForThis.dotProduct(vectorForAnother)
}
