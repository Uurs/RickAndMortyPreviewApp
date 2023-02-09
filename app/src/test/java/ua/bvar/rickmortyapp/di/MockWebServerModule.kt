package ua.bvar.rickmortyapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import ua.bvar.data.di.UrlProviderModule
import ua.bvar.data.repository.UrlProvider

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UrlProviderModule::class]
)
class MockWebServerModule {

    @Provides
    fun provideUrlProvider(): UrlProvider {
        return object : UrlProvider {
            override fun getBaseUrl(): String = mockWebServer.url("/").toString()
        }
    }

    companion object {
        val mockWebServer = MockWebServer()

        const val GET_CHARACTER_DETAILS_ID_1 = "get_character_details_id_1.json"
        const val GET_CHARACTERS_PAGE_1 = "get_characters_page_1.json"
        const val GET_CHARACTERS_PAGE_2 = "get_characters_page_2.json"
        const val GET_CHARACTERS_PAGE_1_NAME_BIRD = "get_characters_page_1_name_bird.json"

        fun enqueueResponseFromAsset(assetsFileName: String) {
            val response = this::class.java.classLoader!!
                .getResourceAsStream("network/$assetsFileName")
                .bufferedReader()
                .use { it.readLines().joinToString("\n") }
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(200)
                    .setBody(response)
            )
        }
    }
}