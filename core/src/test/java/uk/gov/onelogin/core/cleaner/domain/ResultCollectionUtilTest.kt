package uk.gov.onelogin.core.cleaner.domain

import java.util.stream.Stream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.measureTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExperimentalCoroutinesApi
class ResultCollectionUtilTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var util: ResultCollectionUtil

    @BeforeTest
    fun setUp() {
        util = object : ResultCollectionUtil { }
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Suppress("SwallowedException") // Just a test
    @Test
    fun `runConcurrentlyForResults Exception handling`() =
        runTest {
            // Given a ResultCollectionUtil
            val results = mutableListOf<String>()
            val suspendFunctions =
                listOf<suspend () -> Result<Unit>>(
                    {
                        results.add("Function 0 complete")
                        Result.success(Unit)
                    },
                    { throw IllegalStateException() },
                    {
                        results.add("Function 2 complete")
                        Result.success(Unit)
                    }
                )
            // When calling runConcurrentlyForResults
            try {
                with(util) {
                    suspendFunctions.runConcurrentlyForResults()
                }
            } catch (e: IllegalStateException) {
                // Ignore
            }
            // Then complete all jobs not throwing exceptions
            assertContains(results, "Function 0 complete")
            assertContains(results, "Function 2 complete")
        }

    @Test
    fun `runConcurrentlyForResults runs concurrently`() =
        runTest {
            // Given a ResultCollectionUtil
            val delayTime = 1000L
            val suspendFunctions =
                listOf<suspend () -> Result<Unit>>(
                    {
                        delay(delayTime)
                        Result.success(Unit)
                    },
                    {
                        delay(delayTime)
                        Result.success(Unit)
                    },
                    {
                        delay(delayTime)
                        Result.success(Unit)
                    },
                    {
                        delay(delayTime)
                        Result.success(Unit)
                    }
                )
            // When calling runConcurrentlyForResults
            val totalTime =
                with(util) {
                    measureTime {
                        suspendFunctions.runConcurrentlyForResults()
                    }
                }
            // Then simultaneously run the coroutines
            println(totalTime.inWholeMilliseconds)
            println(delayTime * suspendFunctions.size)
            assertTrue(totalTime.inWholeMilliseconds < delayTime * suspendFunctions.size)
        }

    @ParameterizedTest
    @MethodSource("suspendFunctionProvider")
    fun `runConcurrentlyForResults completes to Result list`(
        cleaners: List<Cleaner>,
        expected: List<Result<Unit>>
    ) = runTest {
        // Given a ResultCollectionUtil
        // When calling runConcurrentlyForResults
        val actual =
            with(util) {
                cleaners.map { it::clean }.runConcurrentlyForResults()
            }
        // Then
        assertIs<List<Result<Unit>>>(actual)
        val expectedSuccess = expected.filter { it.isSuccess }
        val actualSuccess = actual.filter { it.isSuccess }
        assertEquals(expected = expectedSuccess.size, actual = actualSuccess.size)
        val expectedFailure = expected.filter { it.isFailure }
        val actualFailure = actual.filter { it.isFailure }
        assertEquals(expected = expectedFailure.size, actual = actualFailure.size)
    }

    @ParameterizedTest
    @MethodSource("combineProvider")
    fun `combineResults only successful on all successful results`(
        results: List<Result<Unit>>,
        expected: Boolean
    ) {
        // Given a ResultCollectionUtil
        // When combing results
        val actual =
            with(util) {
                results.combineResults()
            }
        // Then return a single result representing all sub-results
        assertEquals(expected = expected, actual = actual.isSuccess)
    }

    @ParameterizedTest
    @MethodSource("throwablesProvider")
    fun `Result list extension property throwables filters failures to get list of Throwable `(
        results: List<Result<Unit>>,
        expected: List<Throwable>
    ) {
        // Given a ResultCollectionUtil
        // When calling the extension property `throwables` on the result list
        val actual =
            with(util) {
                results.throwables
            }
        // Then return the correct number of `Throwable` classes
        assertEquals(expected = expected.size, actual = actual.size)
    }

    companion object {
        @JvmStatic
        fun suspendFunctionProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(emptyList<Cleaner>(), emptyList<Result<Unit>>()),
                Arguments.of(
                    listOf(
                        Cleaner { Result.success(Unit) }
                    ),
                    listOf(Result.success(Unit))
                ),
                Arguments.of(
                    listOf(
                        Cleaner { Result.success(Unit) },
                        Cleaner { Result.success(Unit) }
                    ),
                    listOf(Result.success(Unit), Result.success(Unit))
                ),
                Arguments.of(
                    listOf(
                        Cleaner {
                            Result.success(Unit)
                        },
                        Cleaner { Result.failure(Exception()) }
                    ),
                    listOf(Result.success(Unit), Result.failure(Exception()))
                ),
                Arguments.of(
                    listOf(Cleaner { Result.failure(Exception()) }),
                    listOf<Result<Unit>>(Result.failure(Exception()))
                ),
                Arguments.of(
                    listOf(
                        Cleaner { Result.failure(Exception()) },
                        Cleaner { Result.failure(Exception()) }
                    ),
                    listOf<Result<Unit>>(
                        Result.failure(Exception()),
                        Result.failure(Exception())
                    )
                )
            )
        }

        @JvmStatic
        fun combineProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(emptyList<Result<Unit>>(), true),
                Arguments.of(listOf(Result.success(Unit)), true),
                Arguments.of(
                    listOf(Result.success(Unit), Result.success(Unit)),
                    true
                ),
                Arguments.of(
                    listOf(Result.success(Unit), Result.failure(Exception())),
                    false
                ),
                Arguments.of(
                    listOf<Result<Unit>>(Result.failure(Exception()), Result.failure(Exception())),
                    false
                )
            )
        }

        @JvmStatic
        fun throwablesProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(emptyList<Result<Unit>>(), emptyList<Throwable>()),
                Arguments.of(listOf(Result.success(Unit)), emptyList<Throwable>()),
                Arguments.of(
                    listOf(Result.success(Unit), Result.success(Unit)),
                    emptyList<Throwable>()
                ),
                Arguments.of(
                    listOf(Result.success(Unit), Result.failure(Exception())),
                    listOf(Exception())
                ),
                Arguments.of(
                    listOf<Result<Unit>>(
                        Result.failure(Exception()),
                        Result.failure(Exception())
                    ),
                    listOf(
                        Exception(),
                        Exception()
                    )
                )
            )
        }
    }
}
