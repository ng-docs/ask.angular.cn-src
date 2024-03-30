package biz.zhizuo.angular.ask.presentation

import biz.zhizuo.angular.ask.application.ChatService
import biz.zhizuo.angular.ask.utils.nextId
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/ask")
class AskController(private val chatService: ChatService, private val objectMapper: ObjectMapper) {
    @GetMapping
    fun ask(question: String): Flux<ServerSentEvent<String>> {
        return chatService.ask(question).map { message ->
            ServerSentEvent.builder<String>()
                .id(nextId())
                .event("message")
                .data(objectMapper.writeValueAsString(message))
                .build()
        }
    }
}
