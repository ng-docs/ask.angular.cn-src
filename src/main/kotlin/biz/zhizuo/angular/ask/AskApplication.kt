package biz.zhizuo.angular.ask

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class AskApplication

fun main(args: Array<String>) {
    runApplication<AskApplication>(*args)
}
