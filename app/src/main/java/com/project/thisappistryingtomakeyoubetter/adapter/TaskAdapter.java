package com.project.thisappistryingtomakeyoubetter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.model.Task;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> tasks;
    private LongClick listener;

    public TaskAdapter(Context context, List<Task> tasks, LongClick listener){
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
        if(task.getDescription().equals("") || task.getDescription() == null){
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setText(task.getDescription());
        }

        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
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

    public static final class TaskViewHolder extends RecyclerView.ViewHolder {

        CheckBox title;
        TextView description;
        CardView card;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            card = itemView.findViewById(R.id.card);
        }
    }
    public interface LongClick{
        void onLongClick(Task task);
    }
}
