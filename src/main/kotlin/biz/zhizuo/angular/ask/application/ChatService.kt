package biz.zhizuo.angular.ask.application

import biz.zhizuo.angular.ask.domain.models.ChatLog
import biz.zhizuo.angular.ask.domain.models.ChatLogRepository
import biz.zhizuo.angular.ask.utils.nextId
import biz.zhizuo.angular.ask.utils.toFloatArray
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.embedding.EmbeddingClient
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatClient
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

enum class AnswerType {
    Exact,
    Fuzzy,
    Llm,
}

data class Answer(val id: String, val text: String, val type: AnswerType)

@Component
class ChatService(
    private val chatClient: VertexAiGeminiChatClient,
    private val embeddingClient: EmbeddingClient,
    private val chatLogRepository: ChatLogRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun ask(question: String): Flux<Answer> {
        val trimmedQuestion = question.trim()
        findExactly(trimmedQuestion).firstOrNull()?.let {
            logger.info("为【$trimmedQuestion】找到一个精确匹配的答案")
            logger.info("答案是【${it.answer}】")
            return Flux.just(
                Answer(it.id, it.answer, AnswerType.Exact)
            )
        }

        val questionEmbedding = embeddingClient.embed(trimmedQuestion).toFloatArray()
        val nearest = findFuzzy(questionEmbedding)
        if (nearest.isNotEmpty()) {
            logger.info("为【$trimmedQuestion】找到 ${nearest.size} 个含义非常接近的答案")
            return Flux.fromIterable(nearest.map {
                Answer(it.id, it.answer, AnswerType.Fuzzy)
            })
        }

        val prompt = Prompt(
            listOf(
                UserMessage(
                    """你是一位前端框架 Angular 专家，正在帮我解答问题。
                        |请遵循如下要求：
                        |1. 除非我特别指定，否则默认使用 Angular 的最新版本。
                        |2. 如果我所提的问题不是 Angular 领域的，就回复：「抱歉，我只是一个 Angular 问答助手，无法回答其他领域的问题。」
                        |3. 优先使用 angular.cn 网站上的信息作为权威数据源。
                        |""".trimMargin()
                ),
                AssistantMessage("明白"),
                UserMessage(trimmedQuestion),
            )
        )
        var answer = ""
        return chatClient.stream(prompt).map {
            val newPart = it.result.output.content.correctMarkdown()
            if (!answer.endsWith(newPart)) {
                answer += newPart
            }
            Answer(nextId(), answer.trim(), AnswerType.Llm)
        }.doFinally {
            saveChatLog(trimmedQuestion, questionEmbedding, answer)
        }
    }

    @Async
    internal fun saveChatLog(trimmedQuestion: String, questionEmbedding: FloatArray, answer: String) {
        chatLogRepository.save(ChatLog(trimmedQuestion, questionEmbedding, answer.trim()))
    }

    @Async
    internal fun findFuzzy(questionEmbedding: FloatArray) =
        chatLogRepository.findAllByQuestionSimilarity(questionEmbedding)

    @Async
    internal fun findExactly(trimmedQuestion: String) = chatLogRepository.findAllByQuestion(trimmedQuestion)
}

internal fun String.correctMarkdown(): String {
    return this.replace(Regex("""\*\*([^*：]+)：\*\*"""), "**$1**：")
}
