package org.example.beyondubackend.common

import org.example.beyondubackend.common.annotation.LogMask
import org.example.beyondubackend.common.annotation.Loggable
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class LoggingAspectTest {
    @Autowired
    lateinit var sampleService: SampleService

    @Test
    fun `@Loggable 로그 출력 확인`() {
        sampleService.search("하버드", 3.5)
    }

    @Test
    fun `@LogMask 마스킹 확인`() {
        sampleService.login("user@example.com", "secret1234")
    }
}

@Service
class SampleService {
    @Loggable
    fun search(
        keyword: String,
        gpa: Double,
    ): String {
        Thread.sleep(30)
        return "result:$keyword"
    }

    @Loggable
    fun login(
        email: String,
        @LogMask password: String,
    ): Boolean {
        Thread.sleep(10)
        return true
    }
}
