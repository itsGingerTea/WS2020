package com.android.fundamentals.workshop03

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.fundamentals.domain.location.Location
import com.android.fundamentals.domain.location.LocationGenerator
import kotlinx.coroutines.launch

class Workshop3ViewModel(
    private val generator: LocationGenerator
) : ViewModel() {

    private val mutableState = MutableLiveData<Boolean>(false)
    val state: LiveData<Boolean> get() = mutableState

    private val mutableLocationList = MutableLiveData<List<Location>>(emptyList())
    val locationList: LiveData<List<Location>> get() = mutableLocationList


    fun addNew() {
        viewModelScope.launch {
            mutableState.value = true

            val newLocation = generator.generateNewLocation()
            val newLocationList = mutableLocationList?.value?.plus(newLocation).orEmpty()
            mutableLocationList?.value = newLocationList

            mutableState.value = false
        }
    }
}