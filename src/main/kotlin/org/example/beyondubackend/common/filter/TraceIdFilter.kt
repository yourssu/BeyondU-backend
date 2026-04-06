package org.example.beyondubackend.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class TraceIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val traceId =
            UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .take(16)
        try {
            MDC.put("traceId", traceId)
            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
