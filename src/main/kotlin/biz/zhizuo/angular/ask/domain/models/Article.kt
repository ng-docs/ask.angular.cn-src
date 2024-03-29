package biz.zhizuo.angular.ask.domain.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
class Article(
    @Id
    val url: String,
    @Column(columnDefinition = "text")
    var original: String,
    /** 原文的 etag */
    var etag: String? = null,
    @Column(columnDefinition = "text")
    var translation: String? = null,
) {
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

    @OneToMany(mappedBy = "article")
    val paragraphs = mutableListOf<Paragraph>()

    fun addParagraph(paragraph: Paragraph): Article {
        paragraphs.add(paragraph)
        return this
    }
}
