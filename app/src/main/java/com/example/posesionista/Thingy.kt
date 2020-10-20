@file:Suppress("Annotator", "Annotator")

package com.example.posesionista

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Thingy constructor(
    var nombre: String = "",
    var valorEnPesos: Int = 0,
    var numeroDeSerie: String = UUID.randomUUID().toString().substring(0, 8),
    var date: Date = Date(),
    var idThingy: String = UUID.randomUUID().toString().substring(0, 8)
) : Parcelable




