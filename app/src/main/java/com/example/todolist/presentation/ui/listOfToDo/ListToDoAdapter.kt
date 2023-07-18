package com.example.todolist.presentation.ui.listOfToDo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.R
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.ui.api.OnCheckedClickListener
import com.example.todolist.presentation.ui.api.OnItemClickListener

/**
 * ListToDoAdapter - класс, который отвечает за создание, управление и переиспользование ViewHolder'ов.
 */
class ListToDoAdapter(
    private val listener: OnItemClickListener,
    private val onCheckedClickListener: OnCheckedClickListener
) : ListAdapter<TodoItem, ListToDoHolder>(Comparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListToDoHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_of_to_do, parent, false)
        return ListToDoHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: ListToDoHolder, position: Int) {
        holder.bind(getItem(position), onCheckedClickListener)
        holder.itemView.setOnClickListener {
            val todoItem = getItem(holder.adapterPosition)
            listener.onItemClick(todoItem)
        }
    }
}
