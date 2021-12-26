package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.project.thisappistryingtomakeyoubetter.view.ColorList
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.activity.FragmentActivity
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.LabelWithTask

class LabelAdapter(
        private val context: Context,
        private val labels: MutableList<LabelWithTask>
) : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    var deleteListener: ((label: Label) -> Unit)? = null
    var editListener: ((label: Label) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_label, parent, false)
        return LabelViewHolder(v)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val label = labels[position].label

        holder.textView.visibility = View.VISIBLE
        holder.textEdit.visibility = View.GONE

        holder.labelName.text = label.name
        label.color?.let { holder.labelIcon.setColorFilter(it) }
        holder.labelCount.text = labels[position].tasks.size.toString()

        holder.cardLayout.setOnLongClickListener {
            holder.textView.visibility = View.GONE
            holder.textEdit.visibility = View.VISIBLE
            holder.editLabel.setText(label.name)
            true
        }

        holder.cardLayout.setOnClickListener {
            val intent = Intent(context, FragmentActivity::class.java)
            intent.putExtra(HistoryFragment.EXTRA_FILTER, label)
            context.startActivity(intent)
        }

        holder.textInputLayout.setEndIconOnClickListener {
            holder.textView.visibility = View.VISIBLE
            holder.textEdit.visibility = View.GONE
            holder.colorView.visibility = View.GONE
            label.name = holder.editLabel.text.toString()
            editListener?.invoke(label)
        }

        holder.textInputLayout.setStartIconOnClickListener {
            holder.colorView.visibility = View.VISIBLE
            holder.colorView.list = context.resources.getIntArray(R.array.color_array).toList()
            holder.colorView.listener = { color ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    holder.textInputLayout.startIconDrawable?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
                } else {
                    holder.editLabel.setTextColor(color)
                }
                label.color = color
                editListener?.invoke(label)
            }
        }

        holder.delete.setOnClickListener {
            popUpMenu(label, it)
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    private fun popUpMenu(label: Label, view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.apply {
            inflate(R.menu.menu_label)

            setOnMenuItemClickListener {
                if(it.itemId == R.id.action_delete_all){
                    deleteListener?.invoke(label)
                }
                true
            }

            show()
        }
    }

    inner class LabelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelIcon: ImageView = view.findViewById(R.id.label_icon)
        val labelName: TextView = view.findViewById(R.id.label_name)
        val labelCount: TextView = view.findViewById(R.id.label_count)
        val cardLayout: CardView = view.findViewById(R.id.card_layout)
        val textView: ConstraintLayout = view.findViewById(R.id.text_view)
        val textEdit: ConstraintLayout = view.findViewById(R.id.text_edit)
        val textInputLayout: TextInputLayout = view.findViewById(R.id.textlayout)
        val editLabel: TextInputEditText = view.findViewById(R.id.edit_label)
        val colorView: ColorList = view.findViewById(R.id.color_view)
        val delete: ImageButton = view.findViewById(R.id.delete)
    }
}