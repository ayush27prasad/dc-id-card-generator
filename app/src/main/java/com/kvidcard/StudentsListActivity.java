package com.kvidcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.kvidcard.adaptors.StudentsAdaptor;
import com.kvidcard.models.StudentItemModel;
import com.kvidcard.models.StudentResponseBodyModel;
import com.kvidcard.retrofit.ApiService;
import com.kvidcard.retrofit.RetrofitClient;
import com.kvidcard.utils.ProgressBarBox;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentsListActivity extends AppCompatActivity {
    Spinner classSpinner, sectionSpinner;
    MaterialButton fetchStudentsBtn;
    RecyclerView recyclerView;
    ArrayList<StudentItemModel> studentsList;
    ArrayList<StudentResponseBodyModel> studentDetailsList;
    StudentsAdaptor myAdaptor;
    String selectedClass, selectedSection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        fetchStudentsBtn = findViewById(R.id.fetchStudentsButton);
        recyclerView = findViewById(R.id.studentsRecyclerView);

        studentsList = new ArrayList<>();
        studentDetailsList = new ArrayList<>();
        myAdaptor = new StudentsAdaptor(this, studentsList,studentDetailsList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdaptor);

        fetchStudentsBtn.setOnClickListener(v -> {
            selectedClass = classSpinner.getSelectedItem().toString();
            selectedSection = sectionSpinner.getSelectedItem().toString();
            ProgressBarBox.showProgressDialog(this,"Fetching...");
            fetchStudents(selectedClass,selectedSection);

        });


    }
    private void fetchStudents(String className, String section) {
        // TODO : API -> GET
        studentsList.clear();
        studentDetailsList.clear();
       /* studentsList.add(new StudentItemModel("9742334907sda",
                "Ayush Prasad",
                "1",
                true));
        studentDetailsList.add(new StudentResponseBodyModel(
                "9742334907sda",
                "Ayush Prasad",
                "12",
                "A",
                1011,
                "27/08/2003",
                "7 Murkihata",
                "Father",
                "Mother",
                6290167736L,
                "AB+",
                true
        ));*/
        myAdaptor.notifyDataSetChanged();

        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        String token = "Basic YWRtaW46ZG91YnRjb25uZWN0";
        apiService.getStudents(className,section,token).enqueue(new Callback<List<StudentResponseBodyModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<StudentResponseBodyModel>> call, @NonNull Response<List<StudentResponseBodyModel>> response) {
                ProgressBarBox.dismissProgressDialog();
                Log.d("onResponse: call",call.toString());
                Log.d("onResponse: response", response.message() + response.errorBody() + response.toString() + response.body());
                int rno = 1;
                if(response.body() != null) {
                    for (StudentResponseBodyModel studentResponseBodyModel : response.body()) {
                        studentsList.add(new StudentItemModel(studentResponseBodyModel.getStudentId(),
                                studentResponseBodyModel.getStudentName(),
                                Integer.toString(rno++),
                                studentResponseBodyModel.getVerified()));
                        studentDetailsList.add(studentResponseBodyModel);
                        Log.d("Student fetched :",studentResponseBodyModel.toString());

                    }

                }
                if(response.body() == null || studentsList.isEmpty()){
                    Toast.makeText(StudentsListActivity.this, "No students found in db", Toast.LENGTH_SHORT).show();
                }
                myAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<StudentResponseBodyModel>> call, @NonNull Throwable t) {
                ProgressBarBox.dismissProgressDialog();
                Toast.makeText(StudentsListActivity.this, "onFailure() Invoked", Toast.LENGTH_SHORT).show();
                Log.d("Error in Request : ",  t.toString() +"::"+ t.getCause());
            }
        });
    }


}