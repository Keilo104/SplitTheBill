package br.edu.scl.ifsp.ads.splitthebill.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Participant(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @NonNull
    var name: String,

    @NonNull
    var itemBought1: String,

    @NonNull
    var amountSpent1: Double,

    @NonNull
    var itemBought2: String,

    @NonNull
    var amountSpent2: Double,

    @NonNull
    var itemBought3: String,

    @NonNull
    var amountSpent3: Double,

    @NonNull
    var totalAmountSpent: Double,
) : Parcelable
