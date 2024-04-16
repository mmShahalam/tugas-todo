package com.example.todo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvTodo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);

        new FetchDataTask().execute();
    }

    private class FetchDataTask extends AsyncTask<Void, Void, List<Todo>> {

        @Override
        protected List<Todo> doInBackground(Void... voids) {
            List<Todo> todoList = new ArrayList<>();
            try {
                URL url = new URL("https://mgm.ub.ac.id/todo.php");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "AppTodoShahal");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                conn.getOutputStream().write("".getBytes());

                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(is)
                    );
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null)
                        sb.append(line);
                    br.close();

                    Gson gson = new Gson();
                    todoList = gson.fromJson(sb.toString(), new TypeToken<List<Todo>>() {}.getType());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return todoList;
        }

        @Override
        protected void onPostExecute(List<Todo> todoList) {
            adapter.setTodoList(todoList);
        }
    }

    private static class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

        private List<Todo> todoList;

        public void setTodoList(List<Todo> todoList) {
            this.todoList = todoList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            return new TodoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
            Todo todo = todoList.get(position);
            holder.textViewWhat.setText(todo.getWhat());
            holder.textViewTime.setText(todo.getTime());
        }

        @Override
        public int getItemCount() {
            return todoList == null ? 0 : todoList.size();
        }

        private static class TodoViewHolder extends RecyclerView.ViewHolder {

            TextView textViewWhat, textViewTime;

            public TodoViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewWhat = itemView.findViewById(R.id.tvWhat);
                textViewTime = itemView.findViewById(R.id.tvTime);
            }
        }
    }
}
