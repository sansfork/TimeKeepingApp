package com.example.timekeepingapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GroupListViewModel(application: Application): AndroidViewModel(application) {
    private val _listItems = MutableStateFlow<List<GroupItem>>(emptyList())
    val listItems = _listItems.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    init {
        viewModelScope.launch {
            context.groupItemFlow().collect { items ->
                _listItems.value = items
            }
        }
    }

    fun AddItem(newItem: GroupItem) {
        viewModelScope.launch {
            val updatedList = _listItems.value + newItem
            context.saveGroupItems(updatedList)
        }
    }

    // ProfileScreen copies should be made before using this
    // Each ProfileScreen should have the same id as the corresponding GroupListItem
    // Maybe during duplication, poach the GroupListItem's id??
    fun RemoveItemById(itemId: Int) {
        viewModelScope.launch {
            val updatedList = _listItems.value.filterNot { it.id == itemId }
            context.saveGroupItems(updatedList)
        }
    }

    fun GetSize(): Int {
        return _listItems.value.size
    }

    fun GetItemById(itemId: Int): GroupItem? {
        return _listItems.value.firstOrNull({ it.id == itemId })
    }
}