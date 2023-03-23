package com.wcg.sunhwa.batch.container

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
abstract class LocalStackContainerBase {

  companion object {

    private val SAMPLE_BUCKET = "sample-bucket"

    @Container
    private val LOCAL_STACK_CONTAINER =
      LocalStackContainer(DockerImageName.parse("localstack/localstack:1.4")).apply {
        withServices(S3)
        withReuse(true)
      }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      LOCAL_STACK_CONTAINER.start()
      LOCAL_STACK_CONTAINER.execInContainer("awslocal", "s3", "mb", "s3://" + SAMPLE_BUCKET)
      registry.add("bucket-name", { SAMPLE_BUCKET })
      registry.add("spring.cloud.aws.endpoint", { LOCAL_STACK_CONTAINER.getEndpointOverride(S3).toString() })
      registry.add("spring.cloud.aws.region.static", LOCAL_STACK_CONTAINER::getRegion)
      registry.add("spring.cloud.aws.credentials.access-key", LOCAL_STACK_CONTAINER::getAccessKey)
      registry.add("spring.cloud.aws.credentials.secret-key", LOCAL_STACK_CONTAINER::getSecretKey)
    }
  }
}
