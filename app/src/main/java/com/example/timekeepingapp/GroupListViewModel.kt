package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupListViewModel: ViewModel() {
    private val _listItems = MutableStateFlow<List<GroupItem>>(emptyList())
    val listItems = _listItems.asStateFlow()

    fun AddItem(newItem: GroupItem) {
        _listItems.value += newItem
    }
}