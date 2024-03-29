package biz.zhizuo.angular.ask.domain.models

import biz.zhizuo.angular.ask.utils.EMBEDDING_SIZE_GOOGLE
import biz.zhizuo.angular.ask.utils.emptyVectorGoogle
import biz.zhizuo.angular.ask.utils.nextId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.Array
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
class Paragraph(
    @Column(length = 8192)
    val original: String,
    @Column(length = 8192)
    var translation: String,
    val ordinal: Int,
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = EMBEDDING_SIZE_GOOGLE)
    var originalEmbedding: FloatArray = emptyVectorGoogle,
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = EMBEDDING_SIZE_GOOGLE)
    var translationEmbedding: FloatArray = emptyVectorGoogle,
) {
    @Id
    private val id: String = nextId()

    @ManyToOne
    lateinit var article: Article

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
}
