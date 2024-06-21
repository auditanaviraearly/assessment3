package org.d3if3077.githubapps.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if3077.githubapps.data.response.GitHubResponse
import org.d3if3077.githubapps.data.response.ItemsItem
import org.d3if3077.githubapps.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _listUser

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loadingScreen

    companion object {
        private const val TAG = "UserViewModel"
    }

    init {
        findGitHub("auditanaviraearly")
    }

    fun findGitHub(query: String) {
        _loadingScreen.value = true
        try {
            val client = ApiConfig.getApiService().getUser(query)
            Log.d("findGitHub data: ", client.toString())
            client.enqueue(object : Callback<GitHubResponse> {
                override fun onResponse(
                    call: Call<GitHubResponse>,
                    response: Response<GitHubResponse>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) { // Now the if block is in the right place
                        _listUser.value = response.body()?.items
                        Log.d("UserViewModel", "Fetched users: ${_listUser.value}")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        } catch (e: Exception) {
            _loadingScreen.value = false
            Log.e(TAG, "onFailure: ${e.message.toString()}")
            // Handle network or other errors (e.g., show error message)
        } finally {
            _loadingScreen.value = false
        }
    }
}