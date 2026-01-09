import express from 'express';
import admin from 'firebase-admin';
import bodyParser from 'body-parser';
import fs from 'fs';

const app = express();
const PORT = 9999;

// STEP 1: Initialize Firebase Admin
admin.initializeApp({
  credential: admin.credential.cert(
    JSON.parse(await fs.promises.readFile("./serviceAccountKey.json", "utf8"))
  ),
  databaseURL: "https://ian-alumni-directory-default-rtdb.firebaseio.com"
});

app.use(bodyParser.json());
app.use(express.urlencoded({ extended: true }));

// ENDPOINT: Send Approval Notification
// Android app calls this when admin clicks "Approve"
app.post("/send-approval", async (req, res) => {
    const { token } = req.body;
    
    // Validate input
    if (!token) {
        return res.status(400).json({ 
            error: "Missing FCM token" 
        });
    }
    
    // Create approval message
    const message = {
        notification: {
            title: "Registration Approved!",
            body: "Your alumni registration has been approved. You can now access the directory."
        },
        data: {
            type: "status_change",
            status: "APPROVED"
        },
        token: token
    };
    
    // Send notification
    try {
        const response = await admin.messaging().send(message);
        console.log("Approval notification sent:", response);
        res.status(200).json({ 
            success: true, 
            message: "Approval notification sent",
            response: response 
        });
    } catch (error) {
        console.error("Failed to send approval notification:", error);
        res.status(500).json({ 
            success: false, 
            error: error.message 
        });
    }
});

// ENDPOINT: Send Rejection Notification
// Android app calls this when admin clicks "Reject"
app.post("/send-rejection", async (req, res) => {
    const { token } = req.body;
    
    // Validate input
    if (!token) {
        return res.status(400).json({ 
            error: "Missing FCM token" 
        });
    }
    
    // Create rejection message
    const message = {
        notification: {
            title: "Registration Rejected",
            body: "Your alumni registration was not approved. Please contact the administrator if you have questions."
        },
        data: {
            type: "status_change",
            status: "REJECTED"
        },
        token: token
    };
    
    // Send notification
    try {
        const response = await admin.messaging().send(message);
        console.log("Rejection notification sent:", response);
        res.status(200).json({ 
            success: true, 
            message: "Rejection notification sent",
            response: response 
        });
    } catch (error) {
        console.error("Failed to send rejection notification:", error);
        res.status(500).json({ 
            success: false, 
            error: error.message 
        });
    }
});

// Start Server
app.listen(PORT, () => {
    console.log(` FCM Notification Server running on http://localhost:${PORT}`);
    console.log(` Endpoints:`);
    console.log(`   POST /send-approval   - Send approval notification`);
    console.log(`   POST /send-rejection  - Send rejection notification`);
});
