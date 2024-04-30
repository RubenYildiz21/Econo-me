package be.helmo.projetmobile.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date
import java.util.UUID

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nom: String,
    val prenom: String)

@Entity
data class Compte(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nom: String,
    val devise: String,
    val solde: Double
)

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nom: String,
    val solde: Double
) {
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Compte::class,
            parentColumns = ["id"],
            childColumns = ["source"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Compte::class,
            parentColumns = ["id"],
            childColumns = ["destination"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Transfere(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val source: Int,
    val destination: Int,
    val montant: Double
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Compte::class,
            parentColumns = ["id"],
            childColumns = ["compte"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

data class Recurance(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nom: String,
    val montant: Double,
    val compte: Int,
    val date: Date,
    val category: Int
) {
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Compte::class,
            parentColumns = ["id"],
            childColumns = ["compteId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nom: String,
    val categoryId: Int,
    val compteId: Int,
    val date: Date,
    val solde: Double,
    val lieu: String,
    val facture: String,
    val type: Boolean,
    val devise: String
) {}

data class CurrencyResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)