package com.kvidcard;

import static com.kvidcard.utils.ProgressBarBox.dismissProgressDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kvidcard.models.StudentResponseBodyModel;
import com.kvidcard.retrofit.ApiService;
import com.kvidcard.retrofit.RetrofitClient;
import com.kvidcard.utils.BackgroundChangeCallback;
import com.kvidcard.utils.ImageProcessor;
import com.kvidcard.utils.ProgressBarBox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    CardView cardView;
    ShapeableImageView idCardImage;
    MaterialTextView name, fathersName, mothersName, className, section, dob, phNo, address;
    MaterialButton uploadImageButton, saveIdCardBtn;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ActivityResultLauncher<Intent> galleryLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 101, REQUEST_EXTERNAL_STORAGE_PERMISSION = 102;
    private boolean isImageFromCamera = false;
    private Uri photoUri;
    StudentResponseBodyModel currentStudent;

    Map<String,String> classRomans = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        classRomans.put("I","1");
        classRomans.put("II","2");
        classRomans.put("III", "3");
        classRomans.put("IV", "4");
        classRomans.put("V", "5");
        classRomans.put("VI", "6");
        classRomans.put("VII", "7");
        classRomans.put("VIII", "8");
        classRomans.put("IX", "9");
        classRomans.put("X", "10");
        classRomans.put("XI", "11");
        classRomans.put("XII", "12");

        name = findViewById(R.id.name_tv);
        fathersName = findViewById(R.id.fathers_name_tv);
        mothersName = findViewById(R.id.mothers_name_tv);
        className = findViewById(R.id.class_tv);
        section = findViewById(R.id.section_tv);
        dob = findViewById(R.id.dob_tv);
        phNo = findViewById(R.id.phno_tv);
        address = findViewById(R.id.address_tv);

        currentStudent = getIntent().getParcelableExtra("StudentDetails");
        Log.d("PUT : StudentRequestBodyModel",currentStudent.toString());
        fillIdCardDetails(currentStudent);

   /*     galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            // Now you can use the bitmap in your processImage method
                            processImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Handle error
                        }
                    }
                });*/

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            // Image selected from gallery
                            Uri imageUri = data.getData();
                            processImageUri(imageUri);
                        } else if (data != null && data.getExtras() != null) {
                            // Image captured from camera
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            processImage(imageBitmap);
                        }

                    }
                });



        idCardImage = findViewById(R.id.id_card_iv);

        cardView = findViewById(R.id.id_card_cv);

        saveIdCardBtn = findViewById(R.id.save_id_card);
        uploadImageButton = findViewById(R.id.upload_image_btn);

        uploadImageButton.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                requestCameraPermission();
            }
            else {
                String[] options = {"Use Camera", "Choose from Gallery"};
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Select Photo")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                // User chose "Use Camera"
                                isImageFromCamera = true;
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // Ensure there's a camera activity to handle the intent
                             if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                      /* //TODO: Create a file and get fileUri
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile(); //TODO: Implement this method to create a file
                                    } catch (IOException ex) {
                                        // Error occurred while creating the File
                                        ex.printStackTrace();
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()  + ".provider", photoFile);
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                        galleryLauncher.launch(takePictureIntent);
                                    }*/

                                    galleryLauncher.launch(takePictureIntent);

                                } else {
                                    Toast.makeText(MainActivity.this, "No camera app found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // User chose "Choose from Gallery"
                                isImageFromCamera = false;
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryLauncher.launch(pickPhoto);
                            }
                        })
                        .show();
            }

        });


        saveIdCardBtn.setOnClickListener(v -> {

            ProgressBarBox.showProgressDialog(this,"Uploading...");
            Bitmap idCardBitmap = getBitmapFromView(cardView);
            StorageReference idCardRef = storageReference.child("/student_id_cards/" + classRomans.get(currentStudent.getClassName()) + "-" + currentStudent.getSection() + "-" + currentStudent.getStudentName() + "-" + System.currentTimeMillis()  + ".png");

            // Convert Bitmap to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            idCardBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            // Upload the byte array to Firebase Storage
            idCardRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                // Handle successful uploads
                idCardRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {

                    // Here you get the download URL for the uploaded image
                    Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, StudentsListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    updateStudentInfo(currentStudent);
                    finish();
                    Log.d("Upload", "Image Upload successful. URL: " + downloadUri.toString());
                    // You can save this URL in your database or use it directly to display the image
//                    saveImageURLtoDatabase(downloadUri);
                    ProgressBarBox.dismissProgressDialog();
                });
            }).addOnFailureListener(exception -> {
                ProgressBarBox.dismissProgressDialog();
                // Handle unsuccessful uploads
                Log.e("Upload", "Image Upload failed: ", exception);
            });
        });
    }

    private void updateStudentInfo(StudentResponseBodyModel updatedStudent) {
        updatedStudent.setVerified(true); //Student is verified now
        // Assuming RetrofitClient is your class for configuring Retrofit
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Retrieve the student ID from your updatedStudent model or however it's stored
        String studentId = updatedStudent.getStudentId();
        String token = "Basic YWRtaW46ZG91YnRjb25uZWN0"; // Your actual auth token

        Call<ResponseBody> call = apiService.updateStudent(studentId, updatedStudent, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    Toast.makeText(getApplicationContext(), "Student updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failure
                    Toast.makeText(getApplicationContext(), "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error (e.g., network error)
                Toast.makeText(getApplicationContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        // Return the file
        return image;
    }



    private void fillIdCardDetails(StudentResponseBodyModel studentDetails) {
        Log.d("Student Details",studentDetails.toString());
        name.setText(studentDetails.getStudentName());
        fathersName.setText(studentDetails.getFathersName());
        mothersName.setText(studentDetails.getMothersName());
        className.setText(studentDetails.getClassName());
        section.setText(studentDetails.getSection());
        dob.setText(studentDetails.getDob());
        phNo.setText(String.format(studentDetails.getMobileNo().toString()));
        address.setText(studentDetails.getAddress());
    }

    public void processImage(Bitmap bitmap) {

        ImageProcessor.removeBackground(bitmap, new BackgroundChangeCallback() {
            @Override
            public void onSuccess(@NonNull Bitmap resultBitmap) {
                // Use the processed bitmap as needed
                /*if (isImageFromCamera || false) {
                    float scale = Math.min(((float)256 / resultBitmap.getWidth()), ((float)192 / resultBitmap.getHeight()));
                    int width = Math.round(scale * resultBitmap.getWidth());
                    int height = Math.round(scale * resultBitmap.getHeight());
                    // Rescale the image from the camera to improve processing or display
                    resultBitmap = Bitmap.createScaledBitmap(resultBitmap,width,height,true);; // Use the rescaled bitmap from now on
                }*/
                idCardImage.setImageBitmap(resultBitmap);
                Log.d("onSuccess:" ,"!!!Bg removal done!!!");
                saveIdCardBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failure
                Log.d("onFailure:" ,"!!!Bg removal failure!!!");
                Toast.makeText(MainActivity.this, "Error processing the image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBitmap(Bitmap bitmap, String filename) {

        FileOutputStream out = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            // PNG is a lossless format, the compression factor (90) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void processImageUri(Uri imageUri) {
        Log.d("TAG","inside processImageUri");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            processImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to access the camera")
                    .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission was denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}