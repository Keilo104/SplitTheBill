package br.edu.scl.ifsp.ads.splitthebill.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Participant::class], version = 1)
abstract class ParticipantRoomDaoDatabase : RoomDatabase() {
    abstract fun getParticipantRoomDao(): ParticipantRoomDao
}