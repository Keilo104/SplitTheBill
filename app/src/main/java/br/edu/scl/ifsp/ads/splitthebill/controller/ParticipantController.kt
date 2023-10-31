package br.edu.scl.ifsp.ads.splitthebill.controller

import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantDao
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantDaoSqlite
import br.edu.scl.ifsp.ads.splitthebill.view.MainActivity

class ParticipantController(mainActivity: MainActivity) {
    private val participantDaoImpl: ParticipantDao = ParticipantDaoSqlite(mainActivity)

    fun insertParticipant(participant: Participant): Int = participantDaoImpl.createParticipant(participant)

    fun getParticipant(id: Int) = participantDaoImpl.retrieveParticipant(id)

    fun getParticipants() = participantDaoImpl.retrieveParticipants()

    fun editParticipant(participant: Participant) = participantDaoImpl.updateParticipant(participant)

    fun removeParticipant(id: Int) = participantDaoImpl.deleteParticipant(id)
}