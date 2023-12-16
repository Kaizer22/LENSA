package ru.arinae_va.lensa.data.datasource.remote

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import ru.arinae_va.lensa.domain.model.SpecialistModel
import ru.arinae_va.lensa.domain.model.SpecialistResponseModel
import java.time.LocalDateTime
import javax.inject.Inject

interface IUserInfoStorage {

    fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit)

    suspend fun createProfile(profile: SpecialistModel)
    suspend fun updateProfile(profile: SpecialistModel)

    suspend fun uploadAvatarImage(userUid: String, imageUri: Uri): Boolean

    // TODO возврат результата операции, а не просто bool
    suspend fun uploadPortfolioImage(userUid: String, imageUri: Uri): Boolean
    suspend fun setUserAvatar(userUid: String, downloadUrl: String): Boolean
    suspend fun addPortfolioPicture(userUid: String, downloadUrl: String): Boolean
    suspend fun deleteAccount(userUid: String): Boolean

    suspend fun getFeed(): List<SpecialistModel>
    suspend fun sendFeedback(userUid: String?, text: String)
}

private const val PROFILES_COLLECTION = "profile"
private const val FEEDBACK_COLLECTION = "feedback"

private const val AVATAR_FIELD = "avatarUrl"
private const val PORTFOLIO_PICTURES_FIELD = "portfolioUrls"
private const val ID_FIELD = "id"

private const val AVATARS_STORAGE_ROOT_FOLDER = "avatars/"
private const val PORTFOLIOS_STORAGE_ROOT_FOLDER = "portfolios/"

class FirebaseUserInfoStorage @Inject constructor() : IUserInfoStorage {
    private val database: FirebaseFirestore = Firebase.firestore
    private val firebaseStorage: StorageReference = Firebase.storage.reference

    override fun checkUid(uid: String, onCheckResult: (isNew: Boolean) -> Unit) {
        database.collection(PROFILES_COLLECTION).document(uid).get()
            .addOnSuccessListener { data ->
                onCheckResult(data.data == null)
            }
            .addOnFailureListener {
                onCheckResult(true)
            }
            .addOnCanceledListener {
                onCheckResult(true)
            }
    }

    override suspend fun createProfile(profile: SpecialistModel) {
        val ref = database.collection(PROFILES_COLLECTION).document(profile.id)
        ref.set(profile)
    }

    override suspend fun updateProfile(profile: SpecialistModel) {
        TODO("Not yet implemented")
    }

    override suspend fun setUserAvatar(userUid: String, downloadUrl: String): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(userUid)
        ref.update(AVATAR_FIELD, downloadUrl)
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun addPortfolioPicture(userUid: String, downloadUrl: String): Boolean {
        var res = false
        val ref = database.collection(PROFILES_COLLECTION).document(userUid)
        ref.update(PORTFOLIO_PICTURES_FIELD, FieldValue.arrayUnion(downloadUrl))
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun deleteAccount(userUid: String): Boolean {
        var res = false
        database.collection(PROFILES_COLLECTION).document(userUid).delete()
            .addOnSuccessListener { res = true }
            .await()
        return res
    }

    override suspend fun getFeed(): List<SpecialistModel> {
        var result = listOf<SpecialistModel>()
        database.collection(PROFILES_COLLECTION)
            .whereNotEqualTo(ID_FIELD, Firebase.auth.currentUser?.uid)
            .get()
            .addOnSuccessListener {
                result = it.documents.mapNotNull { doc ->
                    doc.toObject<SpecialistResponseModel>()?.mapToSpecialistModel()
                }
            }
            .addOnFailureListener {
                result = listOf()
            }
            .await()
        return result
    }

    override suspend fun sendFeedback(userUid: String?, text: String) {
        database.collection(FEEDBACK_COLLECTION).add(
            FeedbackModel(
                userUid = userUid,
                text = text,
                timestamp = LocalDateTime.now().toString()
            )
        )
    }

    override suspend fun uploadAvatarImage(userUid: String, imageUri: Uri): Boolean {
        val imageRef = firebaseStorage.child(
            AVATARS_STORAGE_ROOT_FOLDER + userUid + "/" + imageUri.lastPathSegment
        )
        val uploadTask = imageRef.putFile(imageUri)

        var uploadResultSuccess = false
        uploadTask.addOnSuccessListener { uploadResultSuccess = true }
            .await()

        return if (uploadResultSuccess) {
            var downloadUrlResultSuccess = false
            val downloadUrl = imageRef.downloadUrl
                .addOnSuccessListener { downloadUrlResultSuccess = true }
                .await()
            if (downloadUrlResultSuccess) {
                return setUserAvatar(userUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
//        uploadTask.addOnSuccessListener {
//
//            downloadUrl.addOnSuccessListener {
//                photo.remoteUri = it.toString()
//                // update our Cloud Firestore with the public image URI.
//                updatePhotoDatabase(specimen, photo)
//            }
//
//        }
//        uploadTask.addOnFailureListener {
//            Log.e(TAG, it.message)
//        }
    }

    override suspend fun uploadPortfolioImage(userUid: String, imageUri: Uri): Boolean {
        val imageRef = firebaseStorage.child(
            PORTFOLIOS_STORAGE_ROOT_FOLDER + userUid + "/" +
                    imageUri.lastPathSegment
        )
        val uploadTask = imageRef.putFile(imageUri)

        var uploadResultSuccess = false
        uploadTask.addOnSuccessListener { uploadResultSuccess = true }
            .await()

        return if (uploadResultSuccess) {
            var downloadUrlResultSuccess = false
            val downloadUrl = imageRef.downloadUrl
                .addOnSuccessListener { downloadUrlResultSuccess = true }
                .await()
            if (downloadUrlResultSuccess) {
                return addPortfolioPicture(userUid, downloadUrl.toString())
            } else {
                false
            }
        } else {
            false
        }
    }
}

data class FeedbackModel(
    val userUid: String?,
    val text: String,
    val timestamp: String,
)
