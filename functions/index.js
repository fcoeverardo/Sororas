const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.myFunctionName = functions.firestore
    .document('evaluations/{userid}').onWrite((change, context) => {
      // ... Your code here
	
	//const n = change.after.data.numChildren();
	var userevaluation = change.after;
	console.log("Wololo");
	//console.log("Mudou ou criou " + n);

    });

 /*   
exports.calculateMedia = functions.firestore
.document(
  "evaluations/{userid}/avaliacoes"
)
.onWrite((change, context) => {
  
    console.log("Mudou ou criou");
    
    const n = change.after.numChildren();
   
 	var userevaluation = change.after;

    var soma = 0;

    console.log("Mudou ou criou " + n);
/*
    userevaluation.ref.once("value", function(snapshot) {
        
        snapshot.forEach(function(childSnapshot) {
            console.log("Nota " + parseFloat(childSnapshot.child("nota").val()));
            soma = soma + parseFloat(childSnapshot.child("nota").val());

        });

        console.log("Soma " + soma);
        const media = parseFloat(soma / n).toFixed(1);
        userevaluation.ref.parent.child("media").set(media);

    });    
    
});
*/