package com.example.yugioh_tcg_deckmaster

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yugioh_tcg_deckmaster.data.datamodels.Deck
import com.example.yugioh_tcg_deckmaster.data.datamodels.Profile
import com.example.yugioh_tcg_deckmaster.data.datamodels.YugiohCard
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class FireBaseViewModel : ViewModel() {

    val auth = Firebase.auth
    val firestore = Firebase.firestore
//    val storage = Firebase.storage

    private val _selectedDeck = MutableLiveData<Deck?>()
    val selectedDeck: LiveData<Deck?>
        get() = _selectedDeck


    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    //region FirebaseUserManagement

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user


    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _myDecks = MutableLiveData<List<Deck>>()
    val myDecks: LiveData<List<Deck>>
        get() = _myDecks


    //Das profile Document enthält ein einzelnes Profil(das des eingeloggten Users)
    //Document ist wie ein Objekt
    lateinit var profileRef: DocumentReference

    init {
        setupUserEnv()
        if (auth.currentUser != null) {
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

    fun register(email: String, password: String, username: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                //User wurde erstellt

                setupUserEnv()

                val newProfile = Profile(email, username)
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


//    fun uploadProfilePicture(uri: Uri) {
//
//        val imageRef = storage.reference.child("images/${auth.currentUser!!.uid}/profilePicture")
//        imageRef.putFile(uri).addOnCompleteListener {
//            if (it.isSuccessful) {
//                imageRef.downloadUrl.addOnCompleteListener { finalImageUrl ->
//                    profileRef.update("profilePicture", finalImageUrl.result.toString())
//                }
//            }
//        }
//    }


    fun getUserName() {
        try {
            profileRef?.get()?.addOnSuccessListener {
                if (it?.exists() == true) {
                    val username = it.getString("username")
                    _username.postValue(username)
                }
            }
        } catch (e: Exception) {
            // Fehler beim Lesen des Dokuments
            Log.e("Firebase", "Fehler beim Lesen des Dokuments", e)
        }
    }

    fun addDeckToFireBase(deck: Deck) {

        val timeStamp = Timestamp.now()

        val deckData = deck.toHashMap()

        profileRef.collection("myDecks").document(timeStamp.toString())
            .set(deckData).addOnSuccessListener {
                Log.e("FireBase", "Deck erfolgreich gespeichert")
            }.addOnFailureListener {
                Log.e("FireBase", "Deck nicht gespeichert $it")
            }
    }

    fun deleteDeckFromFireBase(deck: Deck) {

        profileRef.collection("myDecks").document(deck.timeStamp.toString())
            .delete().addOnSuccessListener {
                Log.e("FireBase", "Deck erfolgreich gelöscht")
            }.addOnFailureListener {
                Log.e("FireBase", "Deck nicht gelöscht $it")
            }
    }

    fun getDecksFromFireBase() {
        val deckRefs = firestore.collection("user").document(auth.uid ?: "")
            .collection("myDecks")

        val decks = mutableListOf<Deck>()

        deckRefs.addSnapshotListener { snapshot, error ->
            decks.clear()
            snapshot?.forEach { document ->

                val deck = document.data.let { Deck.fromMap(it) }

                decks.add(deck)
            }
            Log.d("FireBase", "$decks")
            _myDecks.postValue(decks)

        }
    }

    fun updateDeckInFirebase(deckId: String, updatedDeck: Deck) {
        val userId = auth.uid

        if (userId != null) {
            val deckReference = firestore.collection("user").document(userId)
                .collection("myDecks").document(deckId)

            Log.d("UpdateDeck", updatedDeck.mainDeck.toString())

            val updateData = updatedDeck.toHashMap()

            deckReference.update(updateData)
                .addOnSuccessListener {
                    Log.d("FireBase", "Deck erfolgreich aktualisiert")
                }
                .addOnFailureListener {
                    Log.e("FireBase", "Fehler beim Aktualisieren des Decks", it)
                }
        } else {
            Log.e("FireBase", "Benutzer nicht authentifiziert")
        }
    }

    fun deleteCardFromDeckInFireBase(yugiohCard: YugiohCard, deck: Deck) {
        // Verweise auf das Dokument
        val docRef = profileRef.collection("myDecks").document(deck.timeStamp.toString())

        // Dokument abrufen
        docRef.get().addOnSuccessListener { document ->
            val deckData = document.data

            // Konvertieren Sie die Daten des Dokuments in ein Deck-Objekt
            val deckFromFirestore = deckData?.let { Deck.fromMap(it) }

            // Überprüfen, ob das Deck erfolgreich abgerufen wurde
            if (deckFromFirestore != null) {
                // Das Hauptdeck aktualisieren, um die Karte zu entfernen
                val updatedMainDeck = deckFromFirestore.mainDeck.toMutableList()
                updatedMainDeck.remove(yugiohCard)
                deckFromFirestore.mainDeck = updatedMainDeck
                _selectedDeck.value = deckFromFirestore

                // Aktualisiertes Deck in Firestore speichern
                docRef.set(deckFromFirestore.toHashMap())
                    .addOnSuccessListener {
                        Log.d("FireBase", "Karte erfolgreich aus dem Deck entfernt")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FireBase", "Fehler beim Speichern des aktualisierten Decks", e)
                    }
            } else {
                Log.e("FireBase", "Deck nicht gefunden")
            }
        }
            .addOnFailureListener { e ->
                Log.e("FireBase", "Fehler beim Abrufen des Dokuments", e)
            }
    }

    fun setSelectedDeck(deck: Deck) {

        _selectedDeck.value = deck
    }

}
