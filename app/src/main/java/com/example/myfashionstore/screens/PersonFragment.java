package com.example.myfashionstore.screens;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.myfashionstore.PersonalFragments.OutFitsFragment;
import com.example.myfashionstore.R;
import com.example.myfashionstore.data.ImageModel;
import com.example.myfashionstore.data.UserProfile;
import com.example.myfashionstore.data.ViewerPagerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PersonFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 12345;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageView;
    private CircleImageView profileImageCV;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String TAG = "authenta";
    private TextView userNameTV;
    private TextView addUserNameBu;
    private String userId;
    private StorageReference mStorageRef;
    private View view;
    private ProgressBar progressBar;
    private TextView followersTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person, container, false);
        toolbar = view.findViewById(R.id.toolBarID);
        tabLayout = view.findViewById(R.id.tabLayoutID);
        viewPager = view.findViewById(R.id.viewPagerID);
        userNameTV = view.findViewById(R.id.usernameTV);
        addUserNameBu = view.findViewById(R.id.addusernameBU);
        profileImageCV = view.findViewById(R.id.profile_imageCV);
        progressBar = view.findViewById(R.id.progressbar);
        followersTV = view.findViewById(R.id.followersTV);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        getUserId();
        getUserName();

        getActivity().setActionBar(toolbar);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        // make the default fragment to start in this viewPager is the FollowingFragment
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.viewPagerID,
                new FollowingFragment()).commit();


        imageView = view.findViewById(R.id.settingsID);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println(TAG + "1= " + mGoogleSignInClient);
                System.out.println(TAG + "3= " + FirebaseAuth.getInstance().getCurrentUser());
                System.out.println(TAG + "5= " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                System.out.println(TAG + "6= " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

                 */
                signOut();
            }
        });


        addUserNameBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserName();
            }
        });

        profileImageCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        //download image
        downloadProfileImageBy_Uri();


        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewerPagerAdapter viewerPagerAdapter = new ViewerPagerAdapter(getActivity().
                getSupportFragmentManager());

        viewerPagerAdapter.addFragment(new OutFitsFragment(), "OUTFITS");
        viewerPagerAdapter.addFragment(new TrendingFragment(), "ITEMS");
        viewerPagerAdapter.addFragment(new RecentFragment(), "CHALLENGES");

        viewPager.setAdapter(viewerPagerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imagePath = data.getData();
            try {
                System.out.println("requestCode = " + requestCode);
                System.out.println("resultCode = " + resultCode);
                System.out.println("data = " + data);
                System.out.println("data.getData() = " + data.getData());

                // here for set the image that i chosen on the imageView and i can use here picasso instead bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                profileImageCV.setImageBitmap(bitmap);

                uploadFile(imagePath);


            } catch (IOException e) {

            }
        }
    }

    private String getFileExtension(Uri uri) { // as it .... just get the extension of image ex: .jpg
        ContentResolver cR = Objects.requireNonNull(getActivity()).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Uri file) {

        if (file != null) {
            progressBar.setVisibility(View.VISIBLE);

            // this part only to get the storage url of my photo
            final String[] photoUrl = {""};
            mStorageRef.child("images/profile" + "." + getFileExtension(file)).getDownloadUrl()
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            photoUrl[0] = task.getResult().toString();
                            Log.d(TAG, "Download url is = " + photoUrl[0]);
                        }
                    });

            // this to upload it (meaning: i get the url first then upload it)
            StorageReference riversRef = mStorageRef.child("images/profile" + "." + getFileExtension(file));
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "uploaded successfully...", Toast.LENGTH_SHORT).show();
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.GONE);
                            ImageModel uploadImage = new ImageModel("my profile", photoUrl[0]);

                            mDatabase.child("Users").child(userId).child("profileImage").setValue(uploadImage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                    followersTV.setText(progress + "");
                }
            });


        } else {
            System.out.println("you did not choose any file");
            Toast.makeText(getActivity(), "No file Selected ", Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadProfileImageBy_Uri() {
        /**
         * if you note that this function play will within your application is offline
         * and that because it use (Glide library)
         * here i use the url of image that i saved it in profileImage child when i uploaded
         * the image from the app
         * so the image will not be download every time you open the activity unless it was changed.
         */
        mDatabase.child("Users").child(userId).child("profileImage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageModel upload = dataSnapshot.getValue(ImageModel.class);
                if (upload != null) {
                    if (upload.getUrl() != null) {

                        System.out.println("my name is = " + upload.getUrl());
                        Glide.with(getActivity()).load(upload.getUrl()).into(profileImageCV);
                        Toast.makeText(getActivity(), "downloaded successfully...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /**
         // another way

         StorageReference islandRef = mStorageRef.child("images/profile.jpg");
         String name = database.getReference().child("Users").child(userId).child("profileImage").child("name").getKey();
         final String nn = name;
         islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override public void onSuccess(Uri uri) {
        Log.e("Tuts+", "uri: " + uri.toString() + nn);
        Glide.with(getActivity()).load(uri.toString()
        ).into(profileImageCV);
        }
        });
         */

    }

    private void downloadProfileImageWithoutUri() {
        StorageReference islandRef = mStorageRef.child("images/profile.jpg");

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println("yes downloaded" + taskSnapshot.getStorage().getName());

                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                profileImageCV.setImageBitmap(bitmap);

                // profileImageCV.setImageURI(Uri.fromFile(finalLocalFile));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void updateUserName() {
        String s = database.getReference("Users").child(userId).getKey();
        System.out.println("my value is = 11 " + s);

        mDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile person = dataSnapshot.getValue(UserProfile.class);
                person.setUsername("beso_zaza");
                mDatabase.child("Users").child(userId).setValue(person);
                userNameTV.setText("beso_zaza");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void getUserName() {
        // Read from the database
        String s = database.getReference("Users").child(userId).getKey();
        System.out.println("my value is = 11 " + s);

        mDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile person = dataSnapshot.getValue(UserProfile.class);
                if (person != null) {
                    if (person.getUsername() != null) {
                        userNameTV.setText(person.getUsername());
                        System.out.println("my name is = " + person.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select your Image"), PICK_IMAGE_REQUEST);
    }

    void getUserId() {
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Google sign out
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println("you existed from google");
                        }
                    });
        }
        getActivity().finish();
        System.out.println(TAG + "44= ");
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


}