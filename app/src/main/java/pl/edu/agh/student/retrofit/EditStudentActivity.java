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

public class EditStudentActivity extends AppCompatActivity {
    EditText firstnameTextView;
    EditText lastnameTextView;
    EditText albumTextView;
    EditText noteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        firstnameTextView = (EditText)findViewById(R.id.firstname);
        lastnameTextView = (EditText)findViewById(R.id.lastname);
        albumTextView = (EditText)findViewById(R.id.album);
        noteTextView = (EditText)findViewById(R.id.note);

        ApiBuilder.getStudentService().getStudentByAlbum(getIntent().getStringExtra("album")).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Student student = response.body();
                firstnameTextView.setText(student.firstname);
                lastnameTextView.setText(student.lastname);
                albumTextView.setText(student.album);
                noteTextView.setText(String.valueOf(student.note));
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.e("Student", call.request().toString());
                Log.e("Student", "Error: " + t.getMessage());
            }
        });
    }

    public void updateStudent(View v) {
        Student student = new Student(
                firstnameTextView.getText().toString(),
                lastnameTextView.getText().toString(),
                albumTextView.getText().toString(),
                Double.parseDouble(noteTextView.getText().toString()));
        ApiBuilder.getStudentService().updateStudent(albumTextView.getText().toString(), student).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                Log.e("TAG", "TAG");
                Toast.makeText(v.getContext().getApplicationContext(), "Student edited", Toast.LENGTH_LONG);
                finishActivity(0);
                Intent intent = new Intent(EditStudentActivity.this, StudentsListActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Log.e("TAG", "TAG" + t.getMessage());
            }
        });
    }
}
