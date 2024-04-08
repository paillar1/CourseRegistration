package com.rohith.courseregistration;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditStudentActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText idEditText;
    private EditText priorityEditText;
    private Button saveButton;

    private DatabaseHelper dbHelper;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.edit_name_edit_text);
        idEditText = findViewById(R.id.edit_id_edit_text);
        priorityEditText = findViewById(R.id.edit_priority_edit_text);
        saveButton = findViewById(R.id.save_button);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studentId = extras.getInt("student_id");
            String name = extras.getString("student_name");
            int id = extras.getInt("student_student_id"); // Corrected key
            String priority = extras.getString("student_priority");

            nameEditText.setText(name);
            idEditText.setText(String.valueOf(id));
            priorityEditText.setText(String.valueOf(priority));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
                Intent intent = new Intent(EditStudentActivity.this, ViewStudentsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateStudent() {
        String name = nameEditText.getText().toString();
        int id = Integer.parseInt(idEditText.getText().toString());
        String priority = priorityEditText.getText().toString();

        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_STUDENT_ID, id);
            values.put(DatabaseHelper.COLUMN_PRIORITY, priority);

            int rowsAffected = db.update(DatabaseHelper.TABLE_NAME, values,
                    DatabaseHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(studentId)});

            if (rowsAffected > 0) {
                setResult(RESULT_OK);
                finish();
            } else {
                // No rows were updated
                // Log an error message or display a toast
                Log.e("EditStudentActivity", "No rows were updated in the database.");
                Toast.makeText(this, "Failed to update student. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any exceptions
            // Log the exception and display an error message
            Log.e("EditStudentActivity", "Error updating student in the database: " + e.getMessage(), e);
            Toast.makeText(this, "An error occurred while updating student. Please try again.", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close(); // Close the database connection
            }
        }
    }

}
