package biz.zhizuo.angular.ask.domain.models

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParagraphRepository : JpaRepository<Paragraph, String> {
    fun findAllByOriginalEmbeddingAndTranslationEmbeddingOrderByArticleUrlAscOrdinalAsc(
        originalEmbedding: FloatArray,
        translationEmbedding: FloatArray
    ): List<Paragraph>
}
