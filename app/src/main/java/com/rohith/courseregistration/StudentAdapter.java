package com.rohith.courseregistration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Context mContext;
    private int mResource;
    private ArrayList<Student> mStudentList;

    private DatabaseHelper dbHelper; // Define dbHelper here

    public StudentAdapter(Context context, int resource, ArrayList<Student> objects, DatabaseHelper dbHelper) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mStudentList = objects;
        this.dbHelper = dbHelper; // Assign dbHelper here
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int id = getItem(position).getStudentID();
        String priority = getItem(position).getPriority();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.student_name);
        TextView tvId = convertView.findViewById(R.id.student_id);
        TextView tvPriority = convertView.findViewById(R.id.student_priority);

        tvName.setText(name);
        tvId.setText(String.valueOf(id));
        tvPriority.setText(String.valueOf(priority));

        Button deleteButton = convertView.findViewById(R.id.delete_button);
        Button editButton = convertView.findViewById(R.id.edit_button);

        // Set click listeners
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete action
                deleteStudent(position);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit action
                editStudent(position);
            }
        });

        return convertView;
    }

    private void deleteStudent(final int position) {
        // Delete student from database and update list
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle deletion
                        Student studentToDelete = mStudentList.get(position);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(DatabaseHelper.TABLE_NAME,
                                DatabaseHelper.COLUMN_ID + "=?",
                                new String[]{String.valueOf(studentToDelete.getId())});
                        mStudentList.remove(position);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void editStudent(int position) {
        // Retrieve student details and open edit form
        Student studentToEdit = mStudentList.get(position);
        Intent intent = new Intent(mContext, EditStudentActivity.class);
        intent.putExtra("student_id", studentToEdit.getId());
        intent.putExtra("student_name", studentToEdit.getName());
        intent.putExtra("student_student_id", studentToEdit.getStudentID());
        intent.putExtra("student_priority", studentToEdit.getPriority());
        mContext.startActivity(intent);
    }
}
