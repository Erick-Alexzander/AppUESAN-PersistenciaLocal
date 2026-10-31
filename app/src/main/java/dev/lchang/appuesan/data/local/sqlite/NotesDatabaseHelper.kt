package dev.lchang.appuesan.data.local.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.lchang.appuesan.data.model.NoteModel

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NOTES = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NOTES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CONTENT TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertNote(content: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CONTENT, content)
        }
        return db.insert(TABLE_NOTES, null, values)
    }

    fun getAllNotes(): List<NoteModel> {
        val notesList = mutableListOf<NoteModel>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_ID, $COLUMN_CONTENT FROM $TABLE_NOTES ORDER BY $COLUMN_ID DESC",
            null
        )
        cursor.use { c ->
            val idIndex = c.getColumnIndexOrThrow(COLUMN_ID)
            val contentIndex = c.getColumnIndexOrThrow(COLUMN_CONTENT)
            while (c.moveToNext()) {
                val id = c.getLong(idIndex)
                val content = c.getString(contentIndex)
                notesList.add(NoteModel(id, content))
            }
        }
        return notesList
    }

    fun deleteNote(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
