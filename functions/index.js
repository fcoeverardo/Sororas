const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const firestore = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
exports.CalculateMedia = functions.database
.ref(
  "userevaluation/{userid}/evaluations"
)
.onWrite((change, context) => {
  
    console.log("Mudou ou criou");
    const n = change.after.numChildren();
    var userevaluation = change.after;

    var soma = 0;

    console.log("Mudou ou criou " + n);

    userevaluation.ref.once("value", function(snapshot) {
        
        snapshot.forEach(function(childSnapshot) {
            console.log("Nota " + parseFloat(childSnapshot.child("nota").val()));
            soma = soma + parseFloat(childSnapshot.child("nota").val());

        });

        console.log("Soma " + soma);
        const media = parseFloat(soma / n).toFixed(1);
        userevaluation.ref.parent.child("media").set(media);

    });    
    
return true;
});

 exports.MessgeNotification = functions.database.ref("messages/{userId}/{senderId}/{messageId}")
  .onCreate((snap, context) => {
    

    const userId = context.params.userId;
    //const receiverId = context.params.receiverId;
    const senderId = context.params.senderId;  
    const message = snap.child("text").val();
    var userName;
    var senderName;
    var fotoperfil;

    //snap.ref.root.child("users/contatos")

    if(snap.child("senderId").val() !== userId){


        var topic = userId;

        const user = snap.ref.root.child("users")
                        .child(userId)
                        .child("name");

        user.once('value', function(snapshot) {
        
            userName = snapshot.val() + "";
        
        
        });

        const name = snap.ref.root.child("users")
                        .child(senderId)
                        .child("name");

        name.once('value', function(snapshot) {
        
            senderName = snapshot.val() + "";

            // Notification details.
       const payload = {
        data:{
            top: topic,
            myId: userId,
            id: senderId,
            nome: senderName,
            myName:userName,
            text: message
        }
       };

        admin.messaging().sendToTopic(topic, payload).then(function(response) {
            // See the MessagingTopicResponse reference documentation for the
            // contents of response.
            return console.log("Successfully sent message:", response);
        }).catch(function(error) {
            return console.log("Error sending message:", error);
        });
           
        });
      
    }

    else
        return true;
});

exports.AnuncioNotification = functions.database.ref("advertisement/{anuncioId}")
  .onCreate((snap, context) => {
    
     const anuncioId = context.params.anuncioId;

     const title = snap.child("titulo").val();
     const foto =  snap.child("fotos/0").val();

     console.log("Titulo:" + title);
     console.log("Foto:" + foto);

     var topic = 'news';


     const payload = {
        data:{
            top: topic,
            anunTitle: title
        }
       };

        admin.messaging().sendToTopic(topic, payload).then(function(response) {
            // See the MessagingTopicResponse reference documentation for the
            // contents of response.
            return console.log("Successfully sent message:", response);
        }).catch(function(error) {
            return console.log("Error sending message:", error);
        });
              
    return true;  
});