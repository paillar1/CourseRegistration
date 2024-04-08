package com.rohith.courseregistration;// AddStudentActivity.java

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText idEditText;
    private EditText priorityEditText;
    private Button addButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.name_edit_text);
        idEditText = findViewById(R.id.id_edit_text);
        priorityEditText = findViewById(R.id.priority_edit_text);
        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                int id = Integer.parseInt(idEditText.getText().toString());
                String priority = priorityEditText.getText().toString();

                addStudent(name, id, priority);
                finish();
            }
        });
    }

    private void addStudent(String name, int id, String priority) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_STUDENT_ID, id);
        values.put(DatabaseHelper.COLUMN_PRIORITY, priority);

        db.insert(DatabaseHelper.TABLE_NAME, null, values);
    }
}
