package com.kvidcard.retrofit;

import com.kvidcard.models.StudentResponseBodyModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("attendance-management/students/{className}/{section}")
    Call<List<StudentResponseBodyModel>> getStudents(
            @Path("className") String className,
            @Path("section") String section,
            @Header("Authorization") String token
    );
    @PUT("id_card/edit_student/{studentId}/")
    Call<ResponseBody> updateStudent(
            @Path("studentId") String studentId,
            @Body StudentResponseBodyModel studentResponseBodyModel,
            @Header("Authorization") String token
    );

}
