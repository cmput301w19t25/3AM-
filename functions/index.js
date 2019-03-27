//const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
'use strict'
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
exports.sendNotification = functions.database.ref('/notifications/{user_id}/{notification_id}').onCreate((change,context) => {
   const user_id = event.data.val();

   console.log('We have a notification from : ', user_id);
});

exports.sendNewMessageNotification = functions.database.ref('notifications/{uid}').onWrite(event=>{
    const uuid = change.params.uid;

    console.log('User to send notification', uuid);

});