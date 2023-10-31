package br.edu.scl.ifsp.ads.splitthebill.model

interface ParticipantDao {
    fun createParticipant(participant: Participant): Int

    fun retrieveParticipant(id: Int): Participant?

    fun retrieveParticipants(): MutableList<Participant>

    fun updateParticipant(participant: Participant): Int

    fun deleteParticipant(id: Int): Int
}