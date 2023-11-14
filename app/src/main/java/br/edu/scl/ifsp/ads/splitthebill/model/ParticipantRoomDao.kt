package br.edu.scl.ifsp.ads.splitthebill.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ParticipantRoomDao : ParticipantDao {
    companion object {
        const val PARTICIPANT_DATABASE_FILE = "the_coolest_room_participant_database_TRUST"
        private const val PARTICIPANT_TABLE = "participant"
        private const val ID_COLUMN = "id"
        private const val NAME_COLUMN = "name"
    }

    @Insert
    override fun createParticipant(participant: Participant)

    @Query("SELECT * FROM $PARTICIPANT_TABLE WHERE $ID_COLUMN = :id")
    override fun retrieveParticipant(id: Int): Participant?

    @Query("SELECT * FROM $PARTICIPANT_TABLE ORDER BY $NAME_COLUMN")
    override fun retrieveParticipants(): MutableList<Participant>

    @Update
    override fun updateParticipant(participant: Participant): Int

    @Delete
    override fun deleteParticipant(participant: Participant): Int
}