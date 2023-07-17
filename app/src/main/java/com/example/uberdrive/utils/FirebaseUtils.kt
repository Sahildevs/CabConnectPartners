package com.example.uberdrive.utils

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirebaseUtils {

    //Instantiating firebase firestore
    private val db = FirebaseFirestore.getInstance()

    //Adds an active driver to the firestore collection
    fun addActiveDriver(map: Map<String, String>, driverId: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        db.collection("Active Drivers")
            .document(driverId).set(map)
            .addOnSuccessListener { onSuccess("success") }
            .addOnFailureListener { onFailure("failed") }

    }

    //Updates the active driver date
    fun updateActiveDriver(driverId: String, map: Map<String, String>) {
        db.collection("Active Drivers")
            .document(driverId)
            .update(map)
    }

    //Removes active driver from the firestore collection
    fun removeActiveDriver(driverId: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        db.collection("Active Drivers")
            .document(driverId).delete()
            .addOnSuccessListener { onSuccess("success") }
            .addOnFailureListener { onFailure("failed") }
    }

    //Listen to incoming ride requests from the firestore collection
    fun getRideRequest(driverId: String, onSuccess: (String) -> Unit, onFailure: (exception: Exception) -> Unit)
    : ListenerRegistration {

        val collectionRef = db.collection("Ride Requests")
        val documentRef = collectionRef.document(driverId)

        //The addSnapshotListener method, establishes a real-time connection to the Firestore database,
        // means the listener will receive updates whenever there are changes to the specified document.

        return documentRef.addSnapshotListener { documentSnapshot, error ->
            if (documentSnapshot != null && documentSnapshot.exists() && documentSnapshot["status"] == "REQUESTED") {
                onSuccess("requested")
            }
        }

    }


    //Update the ride request status accept/reject
    fun updateRideRequestStatus(driverId: String, map: Map<String, String>) {
        db.collection("Ride Requests")
            .document(driverId)
            .update(map)
    }
}