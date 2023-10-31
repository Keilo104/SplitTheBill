package br.edu.scl.ifsp.ads.splitthebill.model

import br.edu.scl.ifsp.ads.splitthebill.controller.ParticipantController

class ParticipantListManager(participantController: ParticipantController) {
    private var participantController: ParticipantController = participantController
    public var participantList: MutableList<Participant> = mutableListOf()
    private var totalPurchaseAmount: Double = 0.0
    private var amountOwedPerPerson: Double = 0.0

    fun getTotalPurchaseAmount(): Double {
        return totalPurchaseAmount
    }

    fun getAmountOwedPerPerson(): Double {
        return amountOwedPerPerson
    }

    fun syncParticipantList(): ParticipantListManager {
        participantList = participantController.getParticipants()
        updateInternalValues()
        return this
    }

    private fun updateInternalValues() {
        totalPurchaseAmount = 0.0

        for (participant in participantList) {
            totalPurchaseAmount += participant.amountSpent
        }

        amountOwedPerPerson = totalPurchaseAmount / participantList.count()
        participantList.sortBy { it.name }
    }

    fun removeParticipantAt(position: Int) {
        participantList.removeAt(position)
        updateInternalValues()
    }

    fun addOrUpdateParticipant(participant: Participant) {
        if(participantList.any { it.id == participant.id }) {
            val position = participantList.indexOfFirst {
                it.id == participant.id
            }
            participantList[position] = participant
            participantController.editParticipant(participant)
        } else {
            val newId = participantController.insertParticipant(participant)
            val newParticipant = Participant(
                newId,
                participant.name,
                participant.amountSpent,
                participant.itemsBought
            )

            participantList.add(newParticipant)
        }

        updateInternalValues()
    }

    fun getParticipantAt(position: Int): Participant {
        return participantList[position]
    }
}