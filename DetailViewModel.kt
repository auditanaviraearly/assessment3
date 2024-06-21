package org.d3if3077.githubapps.ui.description

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.d3if3077.githubapps.data.local.entity.FavoriteUser
import org.d3if3077.githubapps.data.response.DetailUserResponse
import org.d3if3077.githubapps.data.response.ItemsItem
import org.d3if3077.githubapps.data.remote.retrofit.ApiConfig
import org.d3if3077.githubapps.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    private val _userDetail = MutableLiveData<FavoriteUser>()
    val userDetail: LiveData<FavoriteUser> = _userDetail

    private val _loadingScreen = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loadingScreen

    private val _userFollowers = MutableLiveData<List<ItemsItem>>()
    val userFollowers: LiveData<List<ItemsItem>> = _userFollowers

    private val _userFollowings = MutableLiveData<List<ItemsItem>>()
    val userFollowings: LiveData<List<ItemsItem>> = _userFollowings

    private var isloaded = false
    private var isfollowerloaded = false
    private var isfollowingloaded = false

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        userRepository.insert(favoriteUser)
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        userRepository.delete(favoriteUser)
    }

    fun getDetailUser(username: String) {
        if (!isloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getDetailUser(username)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        val resBody = response.body()
                        if (resBody != null) {
                            viewModelScope.launch {
                                val isFavorite = userRepository.isFavorite(resBody.login)
                                val currentUser = FavoriteUser(
                                    username = resBody.login,
                                    name = resBody.name,
                                    avatarUrl = resBody.avatarUrl,
                                    followersCount = resBody.followers.toString(),
                                    followingCount = resBody.following.toString(),
                                    isFavorite = isFavorite
                                )
                                _userDetail.postValue(currentUser)
                            }
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isloaded = true
        }
    }

    fun getFollowers(username: String) {
        if (!isfollowerloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getFollowers(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollowers.postValue(response.body())

                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowerloaded = true
        }
    }

    fun getFollowings(username: String) {
        if (!isfollowingloaded) {
            _loadingScreen.value = true
            val client = ApiConfig.getApiService().getFollowing(username)
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _loadingScreen.value = false
                    if (response.isSuccessful) {
                        _userFollowings.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _loadingScreen.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
            isfollowingloaded = true
        }
    }
}