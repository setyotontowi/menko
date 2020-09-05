package com.project.thisappistryingtomakeyoubetter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;

    public TaskAdapter(Context context, List<Task> tasks){
        this.tasks = tasks;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.title.setText(task.getTitle());
        if(task.getDescription().equals("") || task.getDescription() == null){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(task.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static final class TaskViewHolder extends RecyclerView.ViewHolder{

        CheckBox title;
        TextView description;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }
}
