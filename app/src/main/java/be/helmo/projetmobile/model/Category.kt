package be.helmo.projetmobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Category(@PrimaryKey val id: UUID, val nom: String, val solde: Double) {
}