package com.kvidcard.adaptors;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.kvidcard.EditStudentDetailsActivity;
import com.kvidcard.MainActivity;
import com.kvidcard.R;
import com.kvidcard.models.StudentItemModel;
import com.kvidcard.models.StudentResponseBodyModel;


import java.util.ArrayList;

public class StudentsAdaptor extends RecyclerView.Adapter<StudentsAdaptor.MyViewHolder> {

    Context context;
    ArrayList<StudentItemModel> studentsList;
    ArrayList<StudentResponseBodyModel> studentDetailsList;


    public StudentsAdaptor(Context context, ArrayList<StudentItemModel> studentsList, ArrayList<StudentResponseBodyModel> studentDetailsList){
        this.context = context;
        this.studentsList = studentsList;
        this.studentDetailsList = studentDetailsList;
    }

    @NonNull
    @Override
    public StudentsAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentsAdaptor.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.student_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsAdaptor.MyViewHolder holder, int position) {
        StudentItemModel currentStudent = studentsList.get(position);
        holder.studentRollNo.setText(currentStudent.getStudentRollNo());
        holder.studentName.setText(currentStudent.getStudentName());


        holder.cardView.setOnClickListener(v -> {
            Log.d("StudentDetails in Adaptor",studentDetailsList.get(position).toString());
            context.startActivity(new Intent(context, EditStudentDetailsActivity.class).putExtra("StudentDetails",studentDetailsList.get(position)));
        });

        if (currentStudent.isVerified()) {
            holder.verified.setVisibility(View.VISIBLE);
            holder.unverified.setVisibility(View.INVISIBLE);
        }
        else {
            holder.unverified.setVisibility(View.VISIBLE);
            holder.verified.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView studentRollNo, studentName, verified, unverified;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.student_item_cv);
            studentRollNo = itemView.findViewById(R.id.roll_no_tv);
            studentName = itemView.findViewById(R.id.student_name_tv);
            verified = itemView.findViewById(R.id.verified);
            unverified = itemView.findViewById(R.id.unverified);
        }
    }
}
