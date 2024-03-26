package com.infinityco.diary.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinityco.diary.connectivity.ConnectivityObserver
import com.infinityco.diary.data.repository.Diaries
import com.infinityco.diary.data.repository.MongoDB
import com.infinityco.diary.model.RequestState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {
    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getDiaries()
//        viewModelScope.launch {
//            connectivity.observe().collect { network = it }
//        }
    }

    fun getDiaries(zonedDateTime: ZonedDateTime? = null) {
        dateIsSelected = zonedDateTime != null
        diaries.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null) {
            observeFilteredDiaries(zonedDateTime = zonedDateTime)
        } else {
            observeAllDiaries()
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeAllDiaries() {
        allDiariesJob = viewModelScope.launch {
            if (::filteredDiariesJob.isInitialized) {
                filteredDiariesJob.cancelAndJoin()
            }
            MongoDB.getAllDiaries().debounce(2000).collect { result ->
                diaries.value = result
            }
        }
    }

    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) {
        filteredDiariesJob = viewModelScope.launch {
            if (::allDiariesJob.isInitialized) {
                allDiariesJob.cancelAndJoin()
            }
            MongoDB.getFilteredDiaries(zonedDateTime = zonedDateTime).collect { result ->
                diaries.value = result
            }
        }
    }

//    fun deleteAllDiaries(
//        onSuccess: () -> Unit,
//        onError: (Throwable) -> Unit
//    ) {
//        if (network == ConnectivityObserver.Status.Available) {
//            val userId = FirebaseAuth.getInstance().currentUser?.uid
//            val imagesDirectory = "images/${userId}"
//            val storage = FirebaseStorage.getInstance().reference
//            storage.child(imagesDirectory)
//                .listAll()
//                .addOnSuccessListener {
//                    it.items.forEach { ref ->
//                        val imagePath = "images/${userId}/${ref.name}"
//                        storage.child(imagePath).delete()
//                            .addOnFailureListener {
//                                viewModelScope.launch(Dispatchers.IO) {
//                                    imageToDeleteDao.addImageToDelete(
//                                        ImageToDelete(
//                                            remoteImagePath = imagePath
//                                        )
//                                    )
//                                }
//                            }
//                    }
//                    viewModelScope.launch(Dispatchers.IO) {
//                        val result = MongoDB.deleteAllDiaries()
//                        if (result is RequestState.Success) {
//                            withContext(Dispatchers.Main) {
//                                onSuccess()
//                            }
//                        } else if (result is RequestState.Error) {
//                            withContext(Dispatchers.Main) {
//                                onError(result.error)
//                            }
//                        }
//                    }
//                }
//                .addOnFailureListener { onError(it) }
//        } else {
//            onError(Exception("No Internet Connection."))
//        }
//    }

}