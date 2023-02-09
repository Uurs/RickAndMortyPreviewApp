package ua.bvar.data.repositoryimpl

import ua.bvar.data.Constants
import ua.bvar.data.repository.UrlProvider
import javax.inject.Inject

class UrlProviderImpl @Inject constructor() : UrlProvider {
    override fun getBaseUrl() = Constants.BASE_URL
}