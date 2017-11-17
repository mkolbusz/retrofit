package pl.edu.agh.student.retrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);
    }


    public void saveNewStudent(View view) {
        EditText firstnameTextView = (EditText)findViewById(R.id.firstname);
        EditText lastnameTextView = (EditText)findViewById(R.id.lastname);
        EditText albumTextView = (EditText)findViewById(R.id.album);
        EditText noteTextView = (EditText)findViewById(R.id.note);

        Student student = new Student(
                firstnameTextView.getText().toString(),
                lastnameTextView.getText().toString(),
                albumTextView.getText().toString(),
                Double.parseDouble(noteTextView.getText().toString()));

        ApiBuilder.getStudentService().saveStudent(student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                finishActivity(0);
                Log.e("TEST", "DUPA");
                Intent intent = new Intent(AddNewStudentActivity.this, StudentsListActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {

            }
        });
    }
}