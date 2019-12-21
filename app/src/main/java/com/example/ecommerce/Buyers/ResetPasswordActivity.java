package com.example.ecommerce.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.CurrentUser.CurrentU;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText phoneNumber, question1, question2;
    Button verifyBtn;
    TextView questionTxt, restPassTitle;
    String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");
        phoneNumber = findViewById(R.id.reset_pass_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        questionTxt = findViewById(R.id.title_quesitons);
        restPassTitle = findViewById(R.id.reset_pass_txt);
        verifyBtn = findViewById(R.id.verify_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {
            restPassTitle.setText("Set Questions");
            questionTxt.setText("Answer the Following Security Questions?");

            verifyBtn.setText("Set");
            displayPreviousAnswers();
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSecurityAnswers();
                }
            });
        }
        if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUserPhoneNumber();
                }
            });
        }
    }

    private void verifyUserPhoneNumber() {
        final String phone = phoneNumber.getText().toString();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String getAnserOne = question1.getText().toString();
                    final String getAnserTwo = question1.getText().toString();

                    if (dataSnapshot.hasChild("Security Questions")) {
                        String answerOne = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                        String answerTwo = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                        if (getAnserOne.equals(answerOne) && getAnserTwo.equals(answerTwo)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("New Password");

                            final EditText newPassword = new EditText(ResetPasswordActivity.this);
                            newPassword.setHint("Write New Password Here..");
                            builder.setView(newPassword);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialogInterface, int i) {
                                    if (!newPassword.getText().toString().equals("")) {
                                        ref.child("password").setValue(newPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "You have succcessfully updated your password", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Security answers are incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "This phone number does not exist", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setSecurityAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")) {
            Toast.makeText(getApplicationContext(), "Please answer both questions", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(CurrentU.currentOnlineUser.getPhone());


            HashMap<String, Object> securityQuesitonsData = new HashMap<>();
            securityQuesitonsData.put("answer1", answer1);
            securityQuesitonsData.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(securityQuesitonsData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "You have set the security questions successfuly", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(CurrentU.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String answer1 = dataSnapshot.child("answer1").getValue().toString();
                    String answer2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(answer1);
                    question2.setText(answer2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
