package pl.edu.agh.student.retrofit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mkolbusz on 10.11.17.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    List<User> users;

    public ListViewAdapter(List<User> users) {
        this.users = users;
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
        User contact = users.get(position);
        holder.firstnameTextView.setText(contact.name);
        holder.lastnameTextView.setText(contact.login);
        holder.passwordTextView.setText(contact.password);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView firstnameTextView;
        public TextView lastnameTextView;
        public TextView passwordTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            firstnameTextView = (TextView)itemView.findViewById(R.id.firstname);
            lastnameTextView = (TextView)itemView.findViewById(R.id.lastname);
            passwordTextView = (TextView)itemView.findViewById(R.id.password);
        }


    }
}
