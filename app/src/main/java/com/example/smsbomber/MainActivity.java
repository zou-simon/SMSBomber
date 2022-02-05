package com.example.smsbomber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText messageInput;
    Button addButton;
    Spinner messageList;
    EditText numberInput;
    Spinner contactList;
    EditText countInput;
    Button sendButton;

    ArrayList<String> messages;
    ArrayList<String> contacts;

    String[] permissions = new String[] {
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CONTACTS
    };

    /**
     * Check if the application have all permissions
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }

    /**
     * Ask permission
     */
    private void requestPermission() {
        if (!hasPermissions(this, permissions))
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.requestPermission();
        this.startService(new Intent(this, SmsService.class));

        messageInput = findViewById(R.id.messageInput);
        addButton = findViewById(R.id.addButton);
        messageList = findViewById(R.id.messageList);
        numberInput = findViewById(R.id.numberInput);
        contactList = findViewById(R.id.contactList);
        countInput = findViewById(R.id.countInput);
        sendButton = findViewById(R.id.sendButton);

        messages = new ArrayList<>();
        contacts = new ArrayList<>();

        // Add message to the list
        addButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            if (message.length() > 0) {
                messages.add(message);
                ArrayAdapter<String> adapterMessage = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, messages);
                messageList.setAdapter(adapterMessage);
            }
        });

        // Get contact list
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            do {
                contacts.add(cursor.getString(columnNumber));
            } while(cursor.moveToNext());

            ArrayAdapter<String> adapterNumber = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, contacts);
            contactList.setAdapter(adapterNumber);
        }
        cursor.close();

        // Send SMS Bomber
        sendButton.setOnClickListener(v -> {
            String message = messageList.getSelectedItem().toString();
            String phoneInput = numberInput.getText().toString();
            String count = countInput.getText().toString();

            String phone = phoneInput.length() > 0 ? phoneInput : contactList.getSelectedItem().toString();

            if (phone.length() > 0 && message.length() > 0 && count.length() > 0) {
                Sms sms = new Sms(phone, message, Integer.parseInt(count));
                sms.send();
            } else
                Toast.makeText(getBaseContext(), "Veuillez completer le formulaire.", Toast.LENGTH_SHORT).show();
        });
    }
}