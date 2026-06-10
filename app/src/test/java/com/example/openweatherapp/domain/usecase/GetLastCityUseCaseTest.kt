package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.domain.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLastCityUseCaseTest {

    private lateinit var repository: SettingsRepository
    private lateinit var useCase: GetLastCityUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetLastCityUseCase(repository)
    }

    @Test
    fun `invoke returns city from repository flow`() = runTest {
        every { repository.lastCityFlow } returns flowOf("Paris")

        val result = useCase().first()

        assertEquals("Paris", result)
    }
}
