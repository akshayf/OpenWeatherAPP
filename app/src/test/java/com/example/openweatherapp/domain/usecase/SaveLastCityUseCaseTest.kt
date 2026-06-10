package com.example.openweatherapp.domain.usecase

import com.example.openweatherapp.domain.repository.SettingsRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveLastCityUseCaseTest {

    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SaveLastCityUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SaveLastCityUseCase(repository)
    }

    @Test
    fun `invoke calls saveCity on repository`() = runTest {
        coEvery { repository.saveCity("Tokyo") } just Runs

        useCase("Tokyo")

        coVerify { repository.saveCity("Tokyo") }
    }
}
