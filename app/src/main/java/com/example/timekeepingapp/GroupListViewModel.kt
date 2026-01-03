package com.example.timekeepingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroupListViewModel: ViewModel() {
    private val _listItems = MutableStateFlow<List<GroupItem>>(emptyList())
    private var _size = 0
    val listItems = _listItems.asStateFlow()

    fun AddItem(newItem: GroupItem) {
        _listItems.value += newItem
        _size += 1
    }

    // ProfileScreen copies should be made before using this
    // Each ProfileScreen should have the same id as the corresponding GroupListItem
    // Maybe during duplication, poach the GroupListItem's id??
    fun RemoveItemById(itemId: Int) {
        _listItems.update {
            list -> list.filterNot { it.id == itemId }
        }
        _size -= 1
    }

    fun GetSize(): Int {
        return _size
    }
}