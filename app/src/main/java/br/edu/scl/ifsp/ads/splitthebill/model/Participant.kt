package br.edu.scl.ifsp.ads.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant(
    var id: Int,
    var name: String,
    var amountSpent: Double,
    var itemsBought: String,
) : Parcelable {

}