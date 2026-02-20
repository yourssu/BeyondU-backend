package org.example.beyondubackend.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Beyond-U API")
                    .version("v1.0.0")
                    .description("교환학생 준비 관리 플랫폼 API 문서")
            )
            .servers(
                listOf(
                    Server().url("/").description("Current Server")
                )
            )
    }
}
