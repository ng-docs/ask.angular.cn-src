package biz.zhizuo.angular.ask.utils

import biz.zhizuo.angular.ask.TestContainerConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Import(TestContainerConfiguration::class)
annotation class SpringBootTestInContainers
