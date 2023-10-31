package br.edu.scl.ifsp.ads.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participant(
    var id: Int,
    var name: String,
    var itemBought1: String,
    var amountSpent1: Double,
    var itemBought2: String,
    var amountSpent2: Double,
    var itemBought3: String,
    var amountSpent3: Double,
    var totalAmountSpent: Double,
) : Parcelable
