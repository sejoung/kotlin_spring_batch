package com.wcg.sunhwa.batch.container

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class ContainerBase {
  companion object {

    private val SAMPLE_BUCKET = "sample-bucket"

    @Container
    private val LOCAL_STACK_CONTAINER =
      LocalStackContainer(DockerImageName.parse("localstack/localstack:1.4")).apply {
        withServices(LocalStackContainer.Service.S3)
        withReuse(true)
      }

    @Container
    private val REDIS_CONTAINER = GenericContainer("redis:6-alpine").apply {
      withExposedPorts(6379)
      withReuse(true)
    }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      LOCAL_STACK_CONTAINER.start()
      LOCAL_STACK_CONTAINER.execInContainer("awslocal", "s3", "mb", "s3://" + SAMPLE_BUCKET)
      registry.add("bucket-name", { SAMPLE_BUCKET })
      registry.add(
        "spring.cloud.aws.endpoint",
        { LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.S3).toString() }
      )
      registry.add("spring.cloud.aws.region.static", LOCAL_STACK_CONTAINER::getRegion)
      registry.add("spring.cloud.aws.credentials.access-key", LOCAL_STACK_CONTAINER::getAccessKey)
      registry.add("spring.cloud.aws.credentials.secret-key", LOCAL_STACK_CONTAINER::getSecretKey)

      REDIS_CONTAINER.start()
      registry.add("spring.redis.host", REDIS_CONTAINER::getHost)
      registry.add("spring.redis.port", REDIS_CONTAINER::getFirstMappedPort)
    }
  }
}
