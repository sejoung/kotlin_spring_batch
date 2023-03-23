package com.wcg.sunhwa.batch.container

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class RedisContainerBase {

  companion object {

    @Container
    private val REDIS_CONTAINER = GenericContainer("redis:6-alpine").apply {
      withExposedPorts(6379)
      withReuse(true)
    }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      REDIS_CONTAINER.start()
      registry.add("spring.redis.host", REDIS_CONTAINER::getHost)
      registry.add("spring.redis.port", REDIS_CONTAINER::getFirstMappedPort)
    }
  }
}
