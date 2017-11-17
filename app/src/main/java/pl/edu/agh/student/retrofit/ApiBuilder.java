package pl.edu.agh.student.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mkolbusz on 10.11.17.
 */

public class ApiBuilder {
    static String API_ENDPOINT = "http://10.0.2.2:2000/";
    public static StudentService studentService = null;

    public static StudentService getStudentService() {
        if(studentService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            studentService = retrofit.create(StudentService.class);
        }
        return studentService;
    }
}
