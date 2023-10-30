package br.edu.scl.ifsp.ads.splitthebill.model

class ParticipantListManager {
    public val participantList: MutableList<Participant> = mutableListOf()
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
        } else {
            participantList.add(participant)
        }

        updateInternalValues()
    }

    fun getParticipantAt(position: Int): Participant {
        return participantList[position]
    }
}