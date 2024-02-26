package com.example.yugioh_tcg_deckmaster

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.Profile
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class FireBaseViewModel : ViewModel() {

    val auth = Firebase.auth
    val firestore = Firebase.firestore
    val storage = Firebase.storage


    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    //region FirebaseUserManagement

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user

    //Das profile Document enthält ein einzelnes Profil(das des eingeloggten Users)
    //Document ist wie ein Objekt
    lateinit var profileRef: DocumentReference

    init {
        setupUserEnv()
        if(auth.currentUser != null) {
            Log.d("userId", auth.currentUser!!.uid)
        }
    }

    //Richtet die Variablen ein die erst eingerichtet werden können
    //wenn der User eingeloggt ist
    fun setupUserEnv() {

        _user.value = auth.currentUser

        //Alternative Schreibweise um auf null Werte zu überprüfen
        auth.currentUser?.let { firebaseUser ->

            profileRef = firestore.collection("user").document(firebaseUser.uid)

        }

    }

    fun register(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                //User wurde erstellt

                setupUserEnv()

                val newProfile = Profile(email)
                profileRef.set(newProfile)


            } else {
                _errorMessage.value = it.exception?.message
            }
        }

    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                //User wurde eingeloggt
                setupUserEnv()
            } else {
                _errorMessage.value = it.exception?.message
            }
        }
    }

    fun logout() {

        auth.signOut()
        setupUserEnv()

    }


    fun uploadProfilePicture(uri: Uri) {

        val imageRef = storage.reference.child("images/${auth.currentUser!!.uid}/profilePicture")
        imageRef.putFile(uri).addOnCompleteListener {
            if (it.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener { finalImageUrl ->
                    profileRef.update("profilePicture", finalImageUrl.result.toString())
                }
            }
        }
    }

}