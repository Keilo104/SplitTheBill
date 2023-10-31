package br.edu.scl.ifsp.ads.splitthebill.model

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.edu.scl.ifsp.ads.splitthebill.R
import java.sql.SQLException

class ParticipantDaoSqlite(context: Context) : ParticipantDao {
    companion object Constant {
        private const val PARTICIPANT_DATABASE_FILE = "the_coolest_participant_database_TRUST"
        private const val PARTICIPANT_TABLE = "participant"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
        private const val ITEM_BOUGHT_1_COLUMN = "item_bought_1"
        private const val AMOUNT_SPENT_1_COLUMN = "amount_spent_1"
        private const val ITEM_BOUGHT_2_COLUMN = "item_bought_2"
        private const val AMOUNT_SPENT_2_COLUMN = "amount_spent_2"
        private const val ITEM_BOUGHT_3_COLUMN = "item_bought_3"
        private const val AMOUNT_SPENT_3_COLUMN = "amount_spent_3"
        private const val TOTAL_AMOUNT_SPENT_COLUMN = "total_amount_spent"
        private const val CREATE_PARTICIPANT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PARTICIPANT_TABLE (" +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$NAME_COLUMN TEXT NOT NULL, " +
                    "$ITEM_BOUGHT_1_COLUMN TEXT NOT NULL," +
                    "$AMOUNT_SPENT_1_COLUMN REAL NOT NULL, " +
                    "$ITEM_BOUGHT_2_COLUMN TEXT NOT NULL," +
                    "$AMOUNT_SPENT_2_COLUMN REAL NOT NULL, " +
                    "$ITEM_BOUGHT_3_COLUMN TEXT NOT NULL," +
                    "$AMOUNT_SPENT_3_COLUMN REAL NOT NULL, " +
                    "$TOTAL_AMOUNT_SPENT_COLUMN REAL NOT NULL " +
                    ");"
    }

    private val participantSQLiteDatabase: SQLiteDatabase

    init {
        participantSQLiteDatabase =
            context.openOrCreateDatabase(PARTICIPANT_DATABASE_FILE, MODE_PRIVATE, null)

        try {
            participantSQLiteDatabase.execSQL(CREATE_PARTICIPANT_TABLE_STATEMENT)

        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.message.toString())
        }
    }

    override fun createParticipant(participant: Participant): Int = participantSQLiteDatabase.insert(
        PARTICIPANT_TABLE,
        null,
        participant.toContentValues()
    ).toInt()

    override fun retrieveParticipant(id: Int): Participant? {
        val cursor = participantSQLiteDatabase.rawQuery(
            "SELECT * FROM $PARTICIPANT_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )

        val participant = if(cursor.moveToFirst()) cursor.rowToParticipant() else null
        cursor.close()
        return participant
    }

    override fun retrieveParticipants(): MutableList<Participant> {
        val participantList = mutableListOf<Participant>()

        val cursor = participantSQLiteDatabase.rawQuery(
            "SELECT * FROM $PARTICIPANT_TABLE ORDER BY $NAME_COLUMN",
            null
        )

        while (cursor.moveToNext()) {
            participantList.add(cursor.rowToParticipant())
        }
        cursor.close()

        return participantList
    }

    override fun updateParticipant(participant: Participant): Int = participantSQLiteDatabase.update(
        PARTICIPANT_TABLE,
        participant.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(participant.id.toString())
    )

    override fun deleteParticipant(id: Int): Int = participantSQLiteDatabase.delete(
        PARTICIPANT_TABLE,
        "$ID_COLUMN = ?",
        arrayOf(id.toString())
    )

    private fun Cursor.rowToParticipant(): Participant = Participant(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(NAME_COLUMN)),
        getString(getColumnIndexOrThrow(ITEM_BOUGHT_1_COLUMN)),
        getDouble(getColumnIndexOrThrow(AMOUNT_SPENT_1_COLUMN)),
        getString(getColumnIndexOrThrow(ITEM_BOUGHT_2_COLUMN)),
        getDouble(getColumnIndexOrThrow(AMOUNT_SPENT_2_COLUMN)),
        getString(getColumnIndexOrThrow(ITEM_BOUGHT_3_COLUMN)),
        getDouble(getColumnIndexOrThrow(AMOUNT_SPENT_3_COLUMN)),
        getDouble(getColumnIndexOrThrow(TOTAL_AMOUNT_SPENT_COLUMN))
    )

    private fun Participant.toContentValues(): ContentValues = with(ContentValues()) {
        put(NAME_COLUMN, name)
        put(ITEM_BOUGHT_1_COLUMN, itemBought1)
        put(AMOUNT_SPENT_1_COLUMN, amountSpent1)
        put(ITEM_BOUGHT_2_COLUMN, itemBought2)
        put(AMOUNT_SPENT_2_COLUMN, amountSpent2)
        put(ITEM_BOUGHT_3_COLUMN, itemBought3)
        put(AMOUNT_SPENT_3_COLUMN, amountSpent3)
        put(TOTAL_AMOUNT_SPENT_COLUMN, totalAmountSpent)
        this
    }
}