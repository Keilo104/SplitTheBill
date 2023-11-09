package br.edu.scl.ifsp.ads.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.controller.ParticipantController
import br.edu.scl.ifsp.ads.splitthebill.databinding.TileParticipantBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import kotlin.math.abs

class ParticipantAdapter(
    context: Context,
    private val participantController: ParticipantController):
ArrayAdapter<Participant>(context, R.layout.tile_participant, participantController.getParticipantList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val participant = participantController.getParticipantAt(position)
        var tpb: TileParticipantBinding? = null

        var participantTileView = convertView
        if(participantTileView == null) {
            tpb = TileParticipantBinding.inflate(
                context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )

            participantTileView = tpb.root

            val tileParticipantHolder = TileParticipantHolder(
                tpb.nameTv,
                tpb.amountSpentValueTv,
                tpb.amountOutstandingTv,
                tpb.amountOutstandingValueTv,
                tpb.itemsBoughtValueTv,
                )

            participantTileView.tag = tileParticipantHolder
        }

        var itemsBought = ""

        if (participant.itemBought1.isNotEmpty()) {
            itemsBought += "${participant.itemBought1}, "
        }

        if (participant.itemBought2.isNotEmpty()) {
            itemsBought += "${participant.itemBought2}, "
        }

        if (participant.itemBought3.isNotEmpty()) {
            itemsBought += "${participant.itemBought3}, "
        }

        itemsBought = itemsBought.dropLast(2)

        val holder = participantTileView.tag as TileParticipantHolder
        val amountOwed = participantController.getAmountOwedPerPerson() - participant.totalAmountSpent
        val absAmountOwed = abs(amountOwed)

        if (amountOwed < 0) {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_to_receive)

            val hexGreen = context.resources.getColor(R.color.to_receive_green)
            holder.nameTv.setTextColor(hexGreen)
            holder.amountOutstandingValueTv.setTextColor(hexGreen)

        } else if (amountOwed.format(2).equals("0.00")) {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_neutral)

            val hexBlack = context.resources.getColor(R.color.black)
            holder.nameTv.setTextColor(hexBlack)
            holder.amountOutstandingValueTv.setTextColor(hexBlack)

        } else {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_to_pay)

            val hexRed = context.resources.getColor(R.color.to_pay_red)
            holder.nameTv.setTextColor(hexRed)
            holder.amountOutstandingValueTv.setTextColor(hexRed)
        }

        holder.nameTv.text = participant.name
        holder.amountSpentValueTv.text = participant.totalAmountSpent.format(2)
        holder.amountOutstandingValueTv.text = absAmountOwed.format(2)
        holder.itemsBoughtValueTv.text = itemsBought

        return participantTileView
    }

    private class TileParticipantHolder(
        val nameTv: TextView,
        val amountSpentValueTv: TextView,
        val amountOutstandingTv: TextView,
        val amountOutstandingValueTv: TextView,
        val itemsBoughtValueTv: TextView
    )

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}