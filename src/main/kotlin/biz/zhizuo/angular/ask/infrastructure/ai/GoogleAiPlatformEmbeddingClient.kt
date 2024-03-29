package biz.zhizuo.angular.ask.infrastructure.ai

import org.springframework.ai.document.Document
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

fun executeCommand(command: String): String {
    val process = ProcessBuilder(command.split("\\s".toRegex()))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()

    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val output = StringBuilder()
    var line: String?

    while (reader.readLine().also { line = it } != null) {
        output.append(line).append("\n")
    }

    val exitCode = process.waitFor()
    if (exitCode != 0) {
        throw RuntimeException("Command execution failed with exit code $exitCode")
    }

    return output.toString().trim()
}

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
