package com.example.roomdatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class ContactViewModel(private val dao: ContactDao): ViewModel() {
    private val _SortType = MutableStateFlow(SortType.FirstName)
    private val contacts = _SortType.flatMapLatest {
        sorttype ->
            when(sorttype) {
                SortType.FirstName -> dao.getContactsOrderedByFirstName()
                SortType.LastName -> dao.getContactsOrderedByLastName()
                SortType.Phonenumber -> dao.getContactsOrderedByPhoneNumber()
            }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    }
    private val _state = MutableStateFlow(ContactState())
    val state = combine(_state, _SortType, contacts) { state, sorttype, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sorttype
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event : ContactEvent){
        when(event) {
            is ContactEvent.DeleteContacts -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contacts)
                }
            }
            ContactEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingContact = false)
                }
            }
            ContactEvent.SavedContact -> {
                val firstName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNumber = state.value.phoneNumber
                if(firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()){
                    return
                }
                val contacts = Contacts(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )
                viewModelScope.launch {
                    dao.upsertContact(contacts)
                }
                _state.update { it.copy(
                    isAddingContact = false,
                    firstName = "",
                    lastName = "",
                    phoneNumber = "")
                }
            }

            is ContactEvent.SetFirstName -> {
                _state.update {it.copy(
                    firstName  = event.firstName
                )}
            }
            is ContactEvent.SetLastName -> {
                _state.update {it.copy(
                    lastName = event.lastName
                )}
            }
            is ContactEvent.SetPhoneNumber -> {
                _state.update {it.copy(
                    phoneNumber = event.phoneNumber
                )}
            }
            ContactEvent.ShowDialog -> {
                _state.update {it.copy(
                    isAddingContact = true
                )}
            }
            is ContactEvent.SortContacts -> {
                _SortType.value = event.sortType
            }
        }
        }
    }