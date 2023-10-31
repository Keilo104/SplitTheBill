package br.edu.scl.ifsp.ads.splitthebill.controller

import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantDao
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantDaoSqlite
import br.edu.scl.ifsp.ads.splitthebill.view.MainActivity

class ParticipantController(mainActivity: MainActivity) {
    private val participantDaoImpl: ParticipantDao = ParticipantDaoSqlite(mainActivity)
    private var participantList: MutableList<Participant> = mutableListOf()
    private var totalPurchaseAmount: Double = 0.0
    private var amountOwedPerPerson: Double = 0.0

    fun getTotalPurchaseAmount(): Double {
        return totalPurchaseAmount
    }

    fun getAmountOwedPerPerson(): Double {
        return amountOwedPerPerson
    }

    fun getParticipantList(): MutableList<Participant> {
        return participantList
    }

    fun insertParticipant(participant: Participant): Int {
        val result = participantDaoImpl.createParticipant(participant)
        updateInternalValues()

        return result
    }

    fun getParticipant(id: Int) = participantDaoImpl.retrieveParticipant(id)

    fun syncParticipantList(): ParticipantController {
        participantList = participantDaoImpl.retrieveParticipants()
        updateInternalValues()
        return this
    }

    fun editParticipant(participant: Participant){
        participantDaoImpl.updateParticipant(participant)
        updateInternalValues()
    }

    fun removeParticipant(position: Int) {
        val participant = getParticipantAt(position)
        participantDaoImpl.deleteParticipant(participant.id)
        participantList.removeAt(position)
        updateInternalValues()
    }

    fun addOrEditParticipant(participant: Participant) {
        if(participantList.any { it.id == participant.id }) {
            val position = participantList.indexOfFirst {
                it.id == participant.id
            }
            participantList[position] = participant
            editParticipant(participant)

        } else {
            val newId = insertParticipant(participant)
            val newParticipant = Participant(
                newId,
                participant.name,
                participant.itemBought1,
                participant.amountSpent1,
                participant.itemBought2,
                participant.amountSpent2,
                participant.itemBought3,
                participant.amountSpent3,
                participant.totalAmountSpent
            )

            participantList.add(newParticipant)
        }

        updateInternalValues()
    }

    fun getParticipantAt(position: Int): Participant {
        return participantList[position]
    }

    private fun updateInternalValues() {
        totalPurchaseAmount = 0.0

        for (participant in participantList) {
            totalPurchaseAmount += participant.totalAmountSpent
        }

        amountOwedPerPerson = totalPurchaseAmount / participantList.count()
        participantList.sortBy { it.name }
    }
}