package com.kvidcard.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StudentResponseBodyModel implements Parcelable {

    @SerializedName("id")
    private String studentId;
    @SerializedName("name")
    private String studentName;
    @SerializedName("class_name")
    private String className;
    @SerializedName("sec")
    private String section;
    @SerializedName("admission_no")
    private Integer admissionNo;
    @SerializedName("dob")
    private String dob;
    @SerializedName("address")
    private String address;
    @SerializedName("fathers_name")
    private String fathersName;
    @SerializedName("mothers_name")
    private String mothersName;
    @SerializedName("mobile")
    private Long mobileNo;
    @SerializedName("blood_group")
    private String bloodGroup;
    @SerializedName("verified")
    private Boolean verified;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(Integer admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public StudentResponseBodyModel() {
    }

    public StudentResponseBodyModel(String studentId, String studentName, String className, String section, Integer admissionNo, String dob, String address, String fathersName, String mothersName, Long mobileNo, String bloodGroup, Boolean verified) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.className = className;
        this.section = section;
        this.admissionNo = admissionNo;
        this.dob = dob;
        this.address = address;
        this.fathersName = fathersName;
        this.mothersName = mothersName;
        this.mobileNo = mobileNo;
        this.bloodGroup = bloodGroup;
        this.verified = verified;
    }
    @Override
    public String toString() {
        return "StudentResponseBodyModel{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", className='" + className + '\'' +
                ", section='" + section + '\'' +
                ", admissionNo=" + admissionNo +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", fathersName='" + fathersName + '\'' +
                ", mothersName='" + mothersName + '\'' +
                ", mobileNo=" + mobileNo +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", verified=" + verified +
                '}';
    }

    // Parcelable implementation

    protected StudentResponseBodyModel(Parcel in) {
        studentId = in.readString();
        studentName = in.readString();
        className = in.readString();
        section = in.readString();
        if (in.readByte() == 0) {
            admissionNo = null;
        } else {
            admissionNo = in.readInt();
        }
        dob = in.readString();
        address = in.readString();
        fathersName = in.readString();
        mothersName = in.readString();
        if (in.readByte() == 0) {
            mobileNo = null;
        } else {
            mobileNo = in.readLong();
        }
        bloodGroup = in.readString();
        byte tmpVerified = in.readByte();
        verified = tmpVerified == 0 ? null : tmpVerified == 1;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(studentName);
        dest.writeString(className);
        dest.writeString(section);
        if (admissionNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(admissionNo);
        }
        dest.writeString(dob);
        dest.writeString(address);
        dest.writeString(fathersName);
        dest.writeString(mothersName);
        if (mobileNo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mobileNo);
        }
        dest.writeString(bloodGroup);
        if (verified == null) {
            dest.writeByte((byte) 2);
        } else {
            dest.writeByte((byte) (verified ? 1 : 0));
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentResponseBodyModel> CREATOR = new Creator<StudentResponseBodyModel>() {
        @Override
        public StudentResponseBodyModel createFromParcel(Parcel in) {
            return new StudentResponseBodyModel(in);
        }

        @Override
        public StudentResponseBodyModel[] newArray(int size) {
            return new StudentResponseBodyModel[size];
        }
    };





}