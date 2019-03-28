'use strict'

const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});

exports.NewNotification = functions.database.ref('notifications/{userID}/{senderID}/{notificationID}').onWrite((change,context) =>{
	  console.log("Test","test123");
	      if (change.before.exists()) {
        return null;
      }
      // Exit when the data is deleted.
      if (!change.after.exists()) {
        return null;
      }
	  
	  const original = change.after.val();
	  const receiverID = context.params.userID;
	  const senderID = context.params.senderID;
	  console.log('We have a new message for', receiverID);
	  console.log('Uppercasing', context.params.userID, context.params.notificationID, context.params.senderID);
	  
const deviceToken = admin.database().ref(`users/${receiverID}/device_token`).once('value');

return deviceToken.then(result => {
	
	const token_id = result.val();
	console.log("The token is:", token_id);
	const payload = {
		notification: {
			title: "New message!",
			body: "You've received a new message",
			icon: "default"
		}
	};
	
	return admin.messaging().sendToDevice(token_id, payload).then( response => {
		return console.log("Messaged");
		});
	
	});
	 
	 
});
