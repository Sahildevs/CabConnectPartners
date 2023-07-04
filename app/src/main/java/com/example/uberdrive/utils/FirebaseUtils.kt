package com.example.uberdrive.utils

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

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
}