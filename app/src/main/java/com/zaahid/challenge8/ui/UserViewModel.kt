package com.zaahid.challenge8.ui

import android.net.Uri
import androidx.lifecycle.*
import androidx.work.*
import com.zaahid.challenge8.wrapper.Resource
import com.zaahid.challenge8.data.repository.LocalRepository
import com.zaahid.challenge8.model.Hasil
import com.zaahid.challenge8.worker.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: LocalRepository, private val workManager: WorkManager) : ViewModel() {
    val listMovieResult: LiveData<Resource<List<Hasil>>> get() = _listMovieResult
    private val _listMovieResult = MutableLiveData<Resource<List<Hasil>>>()

    internal  val outputWorksInfos:LiveData<List<WorkInfo>> = workManager
        .getWorkInfosByTagLiveData(TAG_OUTPUT)
    internal var imageUri: Uri? = null
    var outputUri: Uri? = null

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {
//        var continuation = workManager
//            .beginWith(OneTimeWorkRequest
//                .from(CleanUpWorker::class.java))
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanUpWorker::class.java)
            )
        for (i in 0 until blurLevel){
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            if (i== 0){
                blurBuilder.setInputData(createInputDataForUri())
            }
            continuation = continuation.then(blurBuilder.build())
        }
        val save = OneTimeWorkRequest
            .Builder(SaveImageToFileWorker::class.java)
            .addTag(TAG_OUTPUT)
            .build()

        continuation= continuation.then(save)

        continuation.enqueue()

    }
    internal fun cancelWork(){
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }
    private fun createInputDataForUri(): Data{
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI,imageUri.toString())
        return builder.build()
    }

    internal fun setOutputUri(outputImageUri: Uri?) {
        outputUri = outputImageUri
    }
    fun setImageUri(uri: Uri?){
        imageUri = uri
    }

    fun getLang(): LiveData<String> {
        return repository.getLang().asLiveData()
    }
    fun setImageString(value: String){
        viewModelScope.launch {
            repository.setImageString(value)
        }
    }
    fun getImageString():LiveData<String>{
        return repository.getImageString().asLiveData()
    }
    fun popularMovie(lang: String = "en-US", page: Int = 1) {
        _listMovieResult.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.popularMovie(lang, page)
            viewModelScope.launch(Dispatchers.Main) {
                _listMovieResult.postValue(data.payload?.let { Resource.Success(it.results) })
            }
        }
    }

    fun searchMovie(query: String, lang: String = "en", page: Int = 1) {
        _listMovieResult.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.searchMovie(query, lang, page)
            viewModelScope.launch(Dispatchers.Main) {
                _listMovieResult.postValue(data.payload?.let { Resource.Success(it.results) })
            }
        }
    }

}