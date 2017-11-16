package pl.edu.agh.student.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mkolbusz on 10.11.17.
 */

public class TestApi {
    private Retrofit retrofit = null;

    public static TestService testService = null;

    public static TestService getService() {
        if(testService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:2000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            testService = retrofit.create(TestService.class);
        }
        return testService;
    }
}
