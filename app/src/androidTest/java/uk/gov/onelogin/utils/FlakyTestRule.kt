package uk.gov.onelogin.utils

import androidx.test.filters.FlakyTest
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@Suppress("TooGenericExceptionCaught")
class FlakyTestRule(private val retryCount: Int = 3) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        val isAnnotationEnabled = description
            .annotations.filterIsInstance(FlakyTest::class.java)
            .isNotEmpty()

        // Determine the actual allowed invocation count based on the FlakyTest annotation
        val actualAllowedInvocationCount = if (isAnnotationEnabled) {
            retryCount
        } else {
            1
        }

        return object : Statement() {
            override fun evaluate() {
                // Retry the test for the specified number of times
                for (i in 0 until actualAllowedInvocationCount) {
                    try {
                        base.evaluate()
                        return
                    } catch (t: Throwable) {
                        // If it's the last retry, throw the exception
                        if (i == actualAllowedInvocationCount - 1) {
                            throw t
                        }
                    }
                }
            }
        }
    }
}
