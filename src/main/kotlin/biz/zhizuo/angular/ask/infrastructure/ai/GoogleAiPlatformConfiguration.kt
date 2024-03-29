package biz.zhizuo.angular.ask.infrastructure.ai

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleAiPlatformConfiguration {
    @Bean
    fun googleAiPlatformClient(): GoogleAiPlatformClient {
        return GoogleAiPlatformClient()
    }

    @Bean
    fun googleAiPlatformEmbeddingClient(googleAiPlatformClient: GoogleAiPlatformClient): GoogleAiPlatformEmbeddingClient {
        return GoogleAiPlatformEmbeddingClient(googleAiPlatformClient)
    }
}
