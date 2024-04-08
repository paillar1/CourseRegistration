package com.rohith.courseregistration;// ViewStudentsActivity.java

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewStudentsActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Student> studentList;
    private StudentAdapter adapter;

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.students_list_view);
        studentList = new ArrayList<>();

        loadStudents();

        adapter = new StudentAdapter(this, R.layout.student_item, studentList,dbHelper);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle click event if needed
            }
        });
    }

    private void loadStudents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                @SuppressLint("Range") int studentID = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_ID));
                @SuppressLint("Range") String priority = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY));

                studentList.add(new Student(id, name, studentID, priority));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }


    private void deleteStudent(int position) {
        // Delete student from database and update list
        Student studentToDelete = studentList.get(position);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME,
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(studentToDelete.getId())});
        studentList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void editStudent(int position) {
        // Retrieve student details and open edit form
        Student studentToEdit = studentList.get(position);
        Intent intent = new Intent(ViewStudentsActivity.this, EditStudentActivity.class);
        intent.putExtra("student_id", studentToEdit.getId());
        intent.putExtra("student_name", studentToEdit.getName());
        intent.putExtra("student_id", studentToEdit.getStudentID());
        intent.putExtra("student_priority", studentToEdit.getPriority());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Update the list view if editing was successful
                loadStudents();
                adapter.notifyDataSetChanged();
            }
        }
    }
}
