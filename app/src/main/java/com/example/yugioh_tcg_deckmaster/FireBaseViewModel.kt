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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

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

            getDecksFromFireBase()
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

        val documentRef = auth.uid?.let { firestore.collection("user").document(it) }

        val timeStamp = Timestamp.now()

        val deckData = hashMapOf(
            "timeStamp" to timeStamp,
            "deckName" to deck.name,
            "mainDeck" to convertCardToMap(deck.mainDeck),
            "extraDeck" to convertCardToMap(deck.extraDeck),
            "sideDeck" to convertCardToMap(deck.sideDeck)
        )

        documentRef?.collection("myDecks")?.document(timeStamp.toString())
            ?.set(deckData)?.addOnSuccessListener {
                Log.e("FireBase", "Deck erfolgreich gespeichert")
            }?.addOnFailureListener {
                Log.e("FireBase", "Deck nicht gespeichert $it")
            }
    }

    fun convertCardToMap(cards: List<YugiohCard>): List<HashMap<String, Any>> {
        return cards.map { card ->
            hashMapOf(
                "id" to card.id,
                "image" to card.card_images.firstOrNull()?.image_url.orEmpty()
            )
        }
    }

    fun getDecksFromFireBase() {
        val deckRefs = firestore.collection("user").document(auth.uid ?: "")
            .collection("myDecks")

        val decks = mutableListOf<Deck>()

        deckRefs.addSnapshotListener { snapshot, error ->
            decks.clear()
            snapshot?.forEach { document ->
                val mainDeckList = document.get("mainDeck") as ArrayList<Map<String, Any?>>
                val mainDeck = mainDeckList.map { YugiohCard.fromMap(it) }

                val extraDeckList = document.get("extraDeck") as ArrayList<Map<String, Any?>>
                val extraDeck = extraDeckList.map { YugiohCard.fromMap(it) }

                val sideDeckList = document.get("sideDeck") as ArrayList<Map<String, Any?>>
                val sideDeck = sideDeckList.map { YugiohCard.fromMap(it) }

                val deck = Deck(
                    mainDeck = mainDeck,
                    extraDeck = extraDeck,
                    sideDeck = sideDeck,
                    name = document.get("deckName") as String,
                    timeStamp = document.get("timeStamp") as Timestamp
                )

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

            val updateData = hashMapOf(
                "deckName" to updatedDeck.name,
                "mainDeck" to convertCardToMap(updatedDeck.mainDeck),
                "extraDeck" to convertCardToMap(updatedDeck.extraDeck),
                "sideDeck" to convertCardToMap(updatedDeck.sideDeck)
                // Füge hier weitere Felder hinzu, wenn notwendig
            ).toMap()

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


}
