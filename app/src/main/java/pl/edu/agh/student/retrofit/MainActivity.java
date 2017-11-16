package pl.edu.agh.student.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<User> users;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        TestApi.getService().getUsers().enqueue(new Callback<List<User>>() {

            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                users = response.body();
                adapter = new ListViewAdapter(users);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });


    }
}
