package br.edu.scl.ifsp.ads.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.splitthebill.R
import br.edu.scl.ifsp.ads.splitthebill.databinding.TileParticipantBinding
import br.edu.scl.ifsp.ads.splitthebill.model.Participant
import br.edu.scl.ifsp.ads.splitthebill.model.ParticipantListManager
import kotlin.math.abs

class ParticipantAdapter(
    context: Context,
    private val participantListManager: ParticipantListManager):
ArrayAdapter<Participant>(context, R.layout.tile_participant, participantListManager.participantList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val participant = participantListManager.getParticipantAt(position)
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
                )

            participantTileView.tag = tileParticipantHolder
        }

        val holder = participantTileView.tag as TileParticipantHolder
        val amountOwed = participantListManager.getAmountOwedPerPerson() - participant.amountSpent
        val absAmountOwed = abs(amountOwed)

        if (amountOwed < 0) {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_to_receive)
            tpb?.amountOutstandingTv?.text = context.resources.getString(R.string.participant_amount_to_receive)

            val hexGreen = context.resources.getColor(R.color.to_receive_green)
            holder.nameTv.setTextColor(hexGreen)
            tpb?.nameTv?.setTextColor(hexGreen)
            holder.amountOutstandingValueTv.setTextColor(hexGreen)
            tpb?.amountOutstandingValueTv?.setTextColor(hexGreen)

        } else if (amountOwed.format(2).equals("0.00")) {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_neutral)
            tpb?.amountOutstandingTv?.text = context.resources.getString(R.string.participant_amount_neutral)

            val hexBlack = context.resources.getColor(R.color.black)
            holder.nameTv.setTextColor(hexBlack)
            tpb?.nameTv?.setTextColor(hexBlack)
            holder.amountOutstandingValueTv.setTextColor(hexBlack)
            tpb?.amountOutstandingValueTv?.setTextColor(hexBlack)

        } else {
            holder.amountOutstandingTv.text = context.resources.getString(R.string.participant_amount_to_pay)
            tpb?.amountOutstandingTv?.text = context.resources.getString(R.string.participant_amount_to_pay)

            val hexRed = context.resources.getColor(R.color.to_pay_red)
            holder.nameTv.setTextColor(hexRed)
            tpb?.nameTv?.setTextColor(hexRed)
            holder.amountOutstandingValueTv.setTextColor(hexRed)
            tpb?.amountOutstandingValueTv?.setTextColor(hexRed)
        }

        holder.nameTv.text = participant.name
        holder.amountSpentValueTv.text = participant.amountSpent.format(2)
        holder.amountOutstandingValueTv.text = absAmountOwed.format(2)

        tpb?.nameTv?.text = participant.name
        tpb?.amountSpentValueTv?.text = participant.amountSpent.format(2)
        tpb?.amountOutstandingValueTv?.text = absAmountOwed.format(2)

        return participantTileView
    }

    private class TileParticipantHolder(
        val nameTv: TextView,
        val amountSpentValueTv: TextView,
        val amountOutstandingTv: TextView,
        val amountOutstandingValueTv: TextView
    )

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}