package org.example.beyondubackend.common.config

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.example.beyondubackend.common.annotation.LogLevel
import org.example.beyondubackend.common.annotation.LogMask
import org.example.beyondubackend.common.annotation.Loggable
import org.example.beyondubackend.common.exception.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {
    @Around(
        "@within(org.example.beyondubackend.common.annotation.Loggable) || " +
            "@annotation(org.example.beyondubackend.common.annotation.Loggable)",
    )
    fun log(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val targetClass = joinPoint.target.javaClass

        val loggable =
            method.getAnnotation(Loggable::class.java)
                ?: targetClass.getAnnotation(Loggable::class.java)
                ?: return joinPoint.proceed()

        val logger = LoggerFactory.getLogger(targetClass)
        val methodName = "${targetClass.simpleName}.${method.name}"
        val args = buildArgString(joinPoint, signature)

        logAt(logger, loggable.level, "[IN]  $methodName args=$args")

        val start = System.currentTimeMillis()
        return try {
            val result = joinPoint.proceed()
            val elapsed = System.currentTimeMillis() - start
            logAt(logger, loggable.level, "[OUT] $methodName elapsed=${elapsed}ms")
            result
        } catch (e: BusinessException) {
            // BusinessException 로깅은 GlobalExceptionHandler에 위임
            throw e
        } catch (e: Exception) {
            val elapsed = System.currentTimeMillis() - start
            logger.error("[ERR] $methodName elapsed=${elapsed}ms exception=${e.message}", e)
            throw e
        }
    }

    private fun buildArgString(
        joinPoint: ProceedingJoinPoint,
        signature: MethodSignature,
    ): String {
        val params = signature.method.parameters
        return joinPoint.args
            .mapIndexed { i, arg ->
                val isMasked = params.getOrNull(i)?.isAnnotationPresent(LogMask::class.java) == true
                if (isMasked) "****" else arg
            }.toString()
    }

    private fun logAt(
        logger: org.slf4j.Logger,
        level: LogLevel,
        message: String,
    ) {
        when (level) {
            LogLevel.INFO -> logger.info(message)
            LogLevel.DEBUG -> logger.debug(message)
        }
    }
}
