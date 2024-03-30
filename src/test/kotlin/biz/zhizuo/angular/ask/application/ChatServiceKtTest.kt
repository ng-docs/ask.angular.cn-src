package biz.zhizuo.angular.ask.application

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class ChatServiceKtTest {

    @Test
    fun correctMarkdown() {
        assertThat("**内容：**".correctMarkdown()).isEqualTo("**内容**：")
        assertThat("**内容*：**".correctMarkdown()).isEqualTo("**内容*：**")
    }
}
