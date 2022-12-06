package io.github.sejoung.kotlin.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestExceptionJob(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
) {

    @Bean
    fun tsvAestheticsJob(): Job {
        return jobBuilderFactory["testExceptionJob"]
            .start(testExceptionStep()).build()
    }

    private fun testExceptionStep(): Step {
        return stepBuilderFactory["testExceptionStep"]
            .tasklet { contribution, chunkContext ->
                throw RuntimeException("Exception test")
                RepeatStatus.FINISHED
            }
            .build()
    }

}
