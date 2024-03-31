package biz.zhizuo.angular.ask.infrastructure.ai

import org.springframework.ai.document.Document
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class GoogleAiPlatformEmbeddingClient(private val platformClient: GoogleAiPlatformClient) : EmbeddingClient {
    @Cacheable("vertex")
    override fun embed(text: String): MutableList<Double> {
        return super.embed(text)
    }

    @Cacheable("vertex")
    override fun embed(texts: MutableList<String>): MutableList<MutableList<Double>> {
        return super.embed(texts)
    }

    override fun call(request: EmbeddingRequest): EmbeddingResponse {
        val modelName = "textembedding-gecko-multilingual@001"

        val instances = request.instructions.map { GoogleAiPlatformClient.Request.Instance(it) }
        if (instances.none { it.content.isNotBlank() }) {
            return EmbeddingResponse(emptyList())
        }

        val result = platformClient.predicate(
            GoogleAiPlatformClient.Request(
                instances,
                GoogleAiPlatformClient.Request.Parameters()
            ), modelName
        )

        return EmbeddingResponse(result.predictions.map { Embedding(it.embeddings.values, 0) })
    }

    override fun embed(document: Document): MutableList<Double> {
        TODO("Not yet implemented")
    }
}
