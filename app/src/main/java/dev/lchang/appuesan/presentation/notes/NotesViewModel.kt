package dev.lchang.appuesan.presentation.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.lchang.appuesan.data.local.sqlite.NotesDatabaseHelper
import dev.lchang.appuesan.data.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = NotesDatabaseHelper(application)

    private val _notes = MutableStateFlow<List<NoteModel>>(emptyList())
    val notes: StateFlow<List<NoteModel>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                dbHelper.getAllNotes()
            }
            _notes.value = list
        }
    }

    fun addNote(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.insertNote(content.trim())
            }
            loadNotes()
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbHelper.deleteNote(id)
            }
            loadNotes()
        }
    }
}
