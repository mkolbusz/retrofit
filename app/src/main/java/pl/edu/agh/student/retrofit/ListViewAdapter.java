package pl.edu.agh.student.retrofit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mkolbusz on 10.11.17.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    List<Student> students;

    public ListViewAdapter(List<Student> students) {
        this.students = students;
    }


    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewAdapter.ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.firstnameTextView.setText(student.lastname + " " + student.firstname);
        holder.noteTextView.setText(String.valueOf(student.note));
        holder.albumTextView.setText(student.album);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstnameTextView;
        public TextView lastnameTextView;
        public TextView noteTextView;
        public TextView albumTextView;
        public Button editButton;
        public Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            firstnameTextView = (TextView)itemView.findViewById(R.id.firstname);
            lastnameTextView = (TextView)itemView.findViewById(R.id.lastname);
            noteTextView = (TextView)itemView.findViewById(R.id.note);
            albumTextView = (TextView)itemView.findViewById(R.id.album);
            editButton = (Button)itemView.findViewById(R.id.editButton);
            removeButton = (Button)itemView.findViewById(R.id.removeButton);

            initActions();
        }

        private void initActions() {
            this.removeButton.setOnClickListener(view -> {
                students.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), students.size());
                Log.e("ERR", albumTextView.getText().toString());
                ApiBuilder.getStudentService().removeStudent(albumTextView.getText().toString()).enqueue(new Callback<Student>() {
                    @Override
                    public void onResponse(Call<Student> call, Response<Student> response) {
                        Toast.makeText(view.getContext(), "Student removed", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(Call<Student> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Student removed", Toast.LENGTH_LONG);
                        Log.e("RETROFIT", "Error while removing student: " + t.getMessage());
                    }
                });

            });

            this.editButton.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), EditStudentActivity.class);
                intent.putExtra("album", albumTextView.getText().toString());
                view.getContext().startActivity(intent);

            });
        }


    }
}
