package com.example.ecommerce.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.CurrentUser.CurrentU;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profileImageView;
    EditText fullNameEditText, userPhoneEditText, addressEditText;
    TextView profileChangeTextBtn, closeButton, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private StorageTask uploadTask;
    Button setSecurityQuestionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = findViewById(R.id.settings_file_image);
        profileChangeTextBtn = findViewById(R.id.profile_image_change);
        closeButton = findViewById(R.id.close_settings);
        saveTextButton = findViewById(R.id.update_settings);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        addressEditText = findViewById(R.id.settings_full_address);
        setSecurityQuestionBtn = findViewById(R.id.set_security_question_btn);
        userInfoDisplay(profileImageView, fullNameEditText, userPhoneEditText, addressEditText);

        setSecurityQuestionBtn.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        saveTextButton.setOnClickListener(this);
        profileChangeTextBtn.setOnClickListener(this);
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentU.currentOnlineUser.getPhone());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        //Load theese when activity starts
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == setSecurityQuestionBtn) {
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            intent.putExtra("check", "settings");
            startActivity(intent);
        }
        if (view == closeButton) {
            finish();
        }
        if (view == saveTextButton) {
            if (checker.equals("clicked")) {
                userInfoSaved();
            } else {
                uupdateOnlyUserInfo();
            }
        }
        if (view == profileChangeTextBtn) {
            checker = "clicked";
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
    }

    private void uupdateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        userMap.put("address", userPhoneEditText.getText().toString());
        userMap.put("phoneOrder", addressEditText.getText().toString());
        ref.child(CurrentU.currentOnlineUser.getPhone())
                .updateChildren(userMap);

        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        Toast.makeText(getApplicationContext(), "Info updated Successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Name is Mandatory", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Address is Mandatory", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Phone", Toast.LENGTH_LONG).show();

        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we're updating your account info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(CurrentU.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri download = task.getResult();
                        myUrl = download.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", fullNameEditText.getText().toString());
                        userMap.put("address", userPhoneEditText.getText().toString());
                        userMap.put("phoneOrder", addressEditText.getText().toString());
                        userMap.put("image", myUrl);

                        ref.child(CurrentU.currentOnlineUser.getPhone())
                                .updateChildren(userMap);

                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(getApplicationContext(), "Info updated Successfully", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Image is not selected.", Toast.LENGTH_LONG).show();

        }
    }
}
