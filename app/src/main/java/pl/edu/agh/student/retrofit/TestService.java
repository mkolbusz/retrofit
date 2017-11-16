package pl.edu.agh.student.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mkolbusz on 10.11.17.
 */

public interface TestService {
    @GET("user/{userId}")
    Call<User> getUserById(@Path("userId") Integer id);

    @GET("users")
    Call<List<User>> getUsers();


}


public interface UserService {
    @GET("user")
    Call<User> getUser(@Header("Authentication") String authHeader);
}

String base = username + ":" + password;
String authHeader = "Basic" + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

Retrofit retrofit = new Retrofit.Builder().baseUrl(API_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                    
UserService userClient = retrofit.create(UserService.class);
Call<User> call = userClient.getUser(authHeader);


OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor(){
    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder().header(
            "Authorization",
            Credentials.basic("aUsername", "aPassword"));
        
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}).build();

Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_ENDPOINT)
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient);