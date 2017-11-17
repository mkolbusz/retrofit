package pl.edu.agh.student.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentsListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Student> students = new ArrayList<>();
    ListViewAdapter adapter;
    String sort = "desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(StudentsListActivity.this, AddNewStudentActivity.class);
            startActivity(intent);
            finishActivity(0);
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(view -> {
           ApiBuilder.getStudentService().getSortedStudentsList(sort).enqueue(new Callback<List<Student>>() {
               @Override
               public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                   students = response.body();
                   adapter.students = students;
                   adapter.notifyDataSetChanged();
                   sort = sort == "desc" ? "asc" : "desc";
               }

               @Override
               public void onFailure(Call<List<Student>> call, Throwable t) {

               }
           });
        });

        recyclerView = (RecyclerView)findViewById(R.id.rvList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new ListViewAdapter(students));

        ApiBuilder.getStudentService().getStudentsList().enqueue(new Callback<List<Student>>() {

            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                students = response.body();
                adapter = new ListViewAdapter(students);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

}
