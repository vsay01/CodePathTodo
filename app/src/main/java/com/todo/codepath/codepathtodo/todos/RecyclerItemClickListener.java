package com.todo.codepath.codepathtodo.todos;

import android.view.View;

public interface RecyclerItemClickListener {
    void onItemClickListener(View view, int position);

    void onItemLongClick(View view, int position);
}