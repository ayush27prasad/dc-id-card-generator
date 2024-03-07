package com.kvidcard.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StudentItemModel {

    private String studentId, studentName, studentRollNo;
    private boolean isVerified = false;



    public StudentItemModel(){}

    public StudentItemModel(String studentId, String studentName, String studentRollNo, boolean isVerified) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentRollNo = studentRollNo;
        this.isVerified = isVerified;
    }


    protected StudentItemModel(Parcel in) {
        studentId = in.readString();
        studentName = in.readString();
        studentRollNo = in.readString();
        isVerified = in.readByte() != 0;
    }

    public static final Parcelable.Creator<StudentItemModel> CREATOR = new Parcelable.Creator<StudentItemModel>() {
        @Override
        public StudentItemModel createFromParcel(Parcel in) {
            return new StudentItemModel(in);
        }

        @Override
        public StudentItemModel[] newArray(int size) {
            return new StudentItemModel[size];
        }
    };

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(studentName);
        dest.writeString(studentRollNo);
        dest.writeByte((byte) (isVerified ? 1 : 0));
    }

    @Override
    public String toString() {
        return "StudentItemModel{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentRollNo='" + studentRollNo + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}
