package biz.zhizuo.angular.ask.domain.models

import biz.zhizuo.angular.ask.utils.AI_TEXT_MAX_LENGTH
import biz.zhizuo.angular.ask.utils.EMBEDDING_SIZE_GOOGLE
import biz.zhizuo.angular.ask.utils.emptyVectorGoogle
import biz.zhizuo.angular.ask.utils.nextId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.Array
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
class ChatLog(
    val question: String,
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = EMBEDDING_SIZE_GOOGLE)
    var questionEmbedding: FloatArray = emptyVectorGoogle,
    @Column(length = AI_TEXT_MAX_LENGTH)
    val answer: String,
    @Id
    val id: String = nextId(),
) {
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    val updatedAt: LocalDateTime? = null
}
