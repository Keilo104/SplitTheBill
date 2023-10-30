package br.edu.scl.ifsp.ads.splitthebill.model

class ParticipantListManager {
    val participantList: MutableList<Participant> = mutableListOf()
    private var totalPurchaseAmount: Double = 0.0
    private var amountOwedPerPerson: Double = 0.0

    fun getTotalPurchaseAmount(): Double {
        return totalPurchaseAmount
    }

    fun getAmountOwedPerPerson(): Double {
        return amountOwedPerPerson
    }

    private fun updateInternalValues() {
        totalPurchaseAmount = 0.0

        for (participant in participantList) {
            totalPurchaseAmount += participant.amountSpent
        }

        amountOwedPerPerson = totalPurchaseAmount / participantList.count()
    }

    fun addOrUpdateParticipant(participant: Participant) {
        if(participantList.any { it.id == participant.id }) {
            val position = participantList.indexOfFirst {
                it.id == participant.id
            }
            participantList[position] = participant
        } else {
            participantList.add(participant)
        }

        updateInternalValues()
    }
}