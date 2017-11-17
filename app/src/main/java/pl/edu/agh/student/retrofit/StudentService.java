package pl.edu.agh.student.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mkolbusz on 10.11.17.
 */

public interface StudentService {
    @GET("student/{album}")
    Call<Student> getStudentByAlbum(@Path("album") String album);

    @GET("students")
    Call<List<Student>> getSortedStudentsList(@Query("sort") String sort);

    @GET("students")
    Call<List<Student>> getStudentsList();

    @POST("student")
    Call<Student> saveStudent(@Body Student student);

    @PUT("student/{album}")
    Call<Student> updateStudent(@Path("album") String album, @Body Student student);

    @DELETE("student/{album}")
    Call<Student> removeStudent(@Path("album") String album);

}