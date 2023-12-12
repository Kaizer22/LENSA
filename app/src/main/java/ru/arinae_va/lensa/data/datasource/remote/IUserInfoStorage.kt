package ru.arinae_va.lensa.data.datasource.remote

import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.domain.model.SpecialistModel
import javax.inject.Inject

interface IUserInfoStorage {
    fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit)

    fun upsertProfile(profile: SpecialistModel)
}

private const val PROFILES_COLLECTION = "profile"
class FirebaseUserInfoStorage @Inject constructor(): IUserInfoStorage {
    private val database: DatabaseReference = Firebase.database.reference

    override fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit) {
        database.child(PROFILES_COLLECTION).child(uid).get()
            .addOnSuccessListener{ data ->
                onCheckResult(false)
            }
            .addOnFailureListener {
                onCheckResult(true)
            }
            .addOnCanceledListener {
                onCheckResult(true)
            }
    }

    override fun upsertProfile(profile: SpecialistModel) {
        TODO("Not yet implemented")
    }

}