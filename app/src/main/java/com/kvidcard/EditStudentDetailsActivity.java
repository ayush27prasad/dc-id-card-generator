package com.kvidcard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.kvidcard.models.StudentResponseBodyModel;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class EditStudentDetailsActivity extends AppCompatActivity {

    TextInputEditText studentNameEt, fathersNameEt, mothersNameEt, phoneEt, addressEt, dobEt, className, section;
    AutoCompleteTextView bloodGroup;
    MaterialButton verifyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_details);

        studentNameEt = findViewById(R.id.student_name_et);
        fathersNameEt = findViewById(R.id.fathers_name_et);
        mothersNameEt = findViewById(R.id.mothers_name_et);
        phoneEt = findViewById(R.id.phone_et);
        addressEt = findViewById(R.id.address_et);
        dobEt = findViewById(R.id.dob_et);
        dobEt.setOnClickListener(v -> showDatePickerDialog());

        className = findViewById(R.id.class_actv);
        section = findViewById(R.id.section_actv);
        className.setOnClickListener(v -> Toast.makeText(this, "You can't change the class", Toast.LENGTH_SHORT).show());
        section.setOnClickListener(v -> Toast.makeText(this, "You can't change the section", Toast.LENGTH_SHORT).show());


        bloodGroup = findViewById(R.id.blood_group_actv);
        String[] bloodGroups = new String[]{"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        bloodGroup.setAdapter(adapter);

        StudentResponseBodyModel studentResponseBodyModel = getIntent().getParcelableExtra("StudentDetails");
        fillIdCardDetails(studentResponseBodyModel);

        verifyBtn = findViewById(R.id.verify_btn);

        verifyBtn.setOnClickListener(v -> {
            startActivity(new Intent(EditStudentDetailsActivity.this, MainActivity.class).putExtra("StudentDetails",fetchFilledIdCardDetails(studentResponseBodyModel)));

        });

    }


    private void showModernDatePickerDialog() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select date of birth");
        final MaterialDatePicker<Long> materialDatePicker = builder.build();

        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            // Use the selected date.
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dobEt.setText(format.format(calendar.getTime()));
        });
    }
    private void fillIdCardDetails(StudentResponseBodyModel studentDetails) {
        Log.d("Student Details in Edit",studentDetails.toString());
        try {
            studentNameEt.setText(studentDetails.getStudentName());
            fathersNameEt.setText(studentDetails.getFathersName());
            mothersNameEt.setText(studentDetails.getMothersName());
            className.setText(studentDetails.getClassName());
            section.setText(studentDetails.getSection());
            dobEt.setText(studentDetails.getDob());
            phoneEt.setText(String.format(studentDetails.getMobileNo().toString()));
            addressEt.setText(studentDetails.getAddress());
            bloodGroup.setText(studentDetails.getBloodGroup());
        }
        catch (Exception e){
            e.printStackTrace();
            studentNameEt.setText("");
            fathersNameEt.setText("");
            mothersNameEt.setText("");
            className.setText("");
            section.setText("");
            dobEt.setText("");
            phoneEt.setText("");
            addressEt.setText("");
            bloodGroup.setText("");
        }
    }
    private StudentResponseBodyModel fetchFilledIdCardDetails(StudentResponseBodyModel studentDetails) {
        try {
            studentDetails.setStudentName(studentNameEt.getText().toString());
            studentDetails.setFathersName(fathersNameEt.getText().toString());
            studentDetails.setMothersName(mothersNameEt.getText().toString());
            studentDetails.setClassName(className.getText().toString());
            studentDetails.setSection(section.getText().toString());
            studentDetails.setDob(dobEt.getText().toString());
            studentDetails.setMobileNo(Long.parseLong(phoneEt.getText().toString()));
            studentDetails.setAddress(addressEt.getText().toString());
            studentDetails.setBloodGroup(bloodGroup.getText().toString());
            Log.d("Student Details in Edit", studentDetails.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("Error in fetchFilledIdCardDetails",studentDetails.toString());
            return null;
        }
        return studentDetails;
    }

    private void showDatePickerDialog() {
        // Default date
        int day = 1,month = 0,year = 2000;
        String separator = dobEt.getText().toString().contains("/")? "/" : "-";
        try{
        String[] ddmmyy = dobEt.getText().toString().split(separator);
        day = Integer.parseInt(ddmmyy[0]);
        month = (Integer.parseInt(ddmmyy[1]) - 1);
        year = Integer.parseInt(ddmmyy[2]);
        Log.d("dates",ddmmyy[0] + ddmmyy[1] + ddmmyy[2]);
        }
        catch (Exception e){
            Toast.makeText(this, "Invalid Date of Birth", Toast.LENGTH_SHORT).show();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    // Format the date chosen by the user
                    String selectedDate = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, yearSelected);
                    dobEt.setText(selectedDate);
                }, year, month, day);

        // Set the DatePicker date range
        Calendar minDate = Calendar.getInstance();
        minDate.set(1990, 0, 1); // 01/01/1990
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        // Set max date to current date
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}