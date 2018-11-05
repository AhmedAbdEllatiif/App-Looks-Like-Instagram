package com.example.ahmedd.firabasetest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = database.getReference("message");

        databaseReference.setValue("Hello world");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  String value = dataSnapshot.getValue(String.class);

                Log.e("Value", value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("DataBase", "Cancelled");
                Log.e("DataBaseError", databaseError.getMessage());

            }
        });

    }
}
