package net.elau.example.kotlin_coroutines_example

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinCoroutinesExampleApplication : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(KotlinCoroutinesExampleApplication::class.java)

    override fun run(vararg args: String?) {
        logger.info("Executing runner...")
        executeBlocking()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun executeBlocking() = runBlocking {
        logger.info("Executing blocking function...")

        val deferredFirstResult: Deferred<String> = GlobalScope.async(start = CoroutineStart.LAZY) {
            getFirstIntegration()
        }
        val deferredSecondResult: Deferred<String> = GlobalScope.async(start = CoroutineStart.LAZY) {
            getSecondIntegration()
        }

        deferredFirstResult.start()
        deferredSecondResult.start()

        logger.info("Finished second routine -> Result=${deferredSecondResult.await()}")
        logger.info("Finished first routine -> Result=${deferredFirstResult.await()}")
    }

    suspend fun getFirstIntegration(): String {
        logger.info("Executing first routine...")
        delay(20000)
        return "{\"result\":\"first\"}"
    }

    suspend fun getSecondIntegration(): String {
        logger.info("Executing second routine...")
        delay(10000)
        return "{\"result\":\"second\"}"
    }
}

fun main(args: Array<String>) {
    runApplication<KotlinCoroutinesExampleApplication>(*args)
}
