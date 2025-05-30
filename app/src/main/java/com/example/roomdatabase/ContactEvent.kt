package com.example.roomdatabase

sealed interface ContactEvent {
    object SavedContact : ContactEvent
    data class SetFirstName(val firstName: String) : ContactEvent
    data class SetLastName(val lastName: String) : ContactEvent
    data class SetPhoneNumber(val phoneNumber : String) : ContactEvent
    object ShowDialog : ContactEvent
    object HideDialog : ContactEvent
    data class SortContacts(val sortType: SortType) : ContactEvent
    data class DeleteContacts(val contacts: Contacts) : ContactEvent
}