package biz.zhizuo.angular.ask.domain.models

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatLogRepository : JpaRepository<ChatLog, String> {
    fun findAllByQuestion(question: String): List<ChatLog>

    @Query("from ChatLog where inner_product(questionEmbedding, :questionEmbedding) > :maxDistance order by inner_product(questionEmbedding, :questionEmbedding) desc limit 10")
    fun findAllByQuestionSimilarity(questionEmbedding: FloatArray, maxDistance: Double): List<ChatLog>
}
