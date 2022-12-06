package io.github.sejoung.kotlin.batch

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import


@SpringBatchTest
@Import(TestExceptionJob::class)
@SpringBootTest
class TestExceptionJobTest {
    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    @Throws(Exception::class)
    fun job() {
        val jobExecution: JobExecution = jobLauncherTestUtils.launchJob()
        Assertions.assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.exitCode)
    }
}
