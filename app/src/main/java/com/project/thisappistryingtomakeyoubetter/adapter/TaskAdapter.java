package com.project.thisappistryingtomakeyoubetter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private List<Task> tasks;
    private final TaskCallback listener;

    public TaskAdapter(Context context, List<Task> tasks, TaskCallback listener){
        this.tasks = tasks;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        final Task task = tasks.get(position);

        holder.title.setText(task.getTitle());
        holder.title.setChecked(task.isFinish());
        if(task.getDescription().equals("") || task.getDescription() == null){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(task.getDescription());
        }

        holder.title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setFinish(isChecked);
                listener.onBoxChecked(task);
            }
        });

        holder.wrapper.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(task);
                return true;
            }
        });

        holder.title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(task);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static final class TaskViewHolder extends RecyclerView.ViewHolder {

        CheckBox title;
        TextView description;
        LinearLayout wrapper;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            wrapper = itemView.findViewById(R.id.wrapper);
        }
    }
    public interface TaskCallback{
        void onLongClick(Task task);
        void onBoxChecked(Task task);
    }
}
