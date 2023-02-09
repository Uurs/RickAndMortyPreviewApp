package ua.bvar.rickmortyapp

import ua.bvar.rickmortyapp.core.BaseViewModel
import ua.bvar.rickmortyapp.di.MockDatabaseModule
import ua.bvar.rickmortyapp.di.MockWebServerModule

abstract class BaseScreenTest<T : BaseViewModel> {

    abstract fun createViewModel(): T

    protected inline fun testEnv(
        dbFileName: String? = null,
        serverResponses: List<String>? = null,
        body: (viewModel: T) -> Unit
    ) {
        serverResponses?.forEach { MockWebServerModule.enqueueResponseFromAsset(it) }

        if (dbFileName != null) {
            MockDatabaseModule.setupFromAssets(dbFileName)
        }
        val viewModel = createViewModel()

        body(viewModel)
    }
}