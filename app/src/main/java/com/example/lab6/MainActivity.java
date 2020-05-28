package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    EditText idEditText;
    EditText nameEditText;
    EditText marksEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = openOrCreateDatabase("Lab6DB", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS students (student_id int, student_name varchar(255), student_mark int);");

        idEditText = findViewById(R.id.id_edit_text);
        nameEditText = findViewById(R.id.name_edit_text);
        marksEditText = findViewById(R.id.marks_edit_text);

        Button addNewStudent = findViewById(R.id.add_new_student_button);
        Button viewAllStudents = findViewById(R.id.view_all_students_button);
        Button findStudent = findViewById(R.id.find_student_button);
        Button deleteStudent = findViewById(R.id.delete_student_button);

        addNewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTextFieldsComplete()) {
                    database.execSQL("INSERT INTO students VALUES ('" + idEditText.getText() + "', '" + nameEditText.getText() + "', '" + marksEditText.getText() + "')");
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("The following student was added");
                    alertDialog.setMessage("ID:" + idEditText.getText().toString() + " Name:" + nameEditText.getText().toString() + " Marks:" + marksEditText.getText().toString());
                    alertDialog.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    clearTextFields();
                    alertDialog.show();
                }
            }
        });

        viewAllStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorSelectAll = database.rawQuery("SELECT * FROM students", null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                StringBuffer allStudents = new StringBuffer();
                while(cursorSelectAll.moveToNext()) {
                    allStudents.append("ID:" + cursorSelectAll.getString(0));
                    allStudents.append(" Name:" + cursorSelectAll.getString(1));
                    allStudents.append(" Marks:" + cursorSelectAll.getString(2));
                    allStudents.append("\n");
                }

                if(allStudents.toString().isEmpty()) {
                    alertDialog.setTitle("No students were found");
                } else {
                    alertDialog.setTitle("The following students have been added");
                    alertDialog.setMessage(allStudents);
                }


                alertDialog.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                clearTextFields();
                alertDialog.show();
            }
        });

        findStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorSelect = database.rawQuery("SELECT * FROM students WHERE student_id == '" + idEditText.getText().toString() +"'", null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                if(cursorSelect.moveToFirst()) {
                    alertDialog.setTitle("Student details are as follows");
                    String foundStudent = new String();
                    foundStudent = "ID: " + cursorSelect.getString(0) + " Name: " + cursorSelect.getString(1) + " Marks: " + cursorSelect.getString(2);
                    alertDialog.setMessage(foundStudent);
                } else {
                    alertDialog.setTitle("This student does not exist");
                }

                alertDialog.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                clearTextFields();
                alertDialog.show();
            }
        });

        deleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorSelect = database.rawQuery("SELECT * FROM students WHERE student_id == '" + idEditText.getText().toString() +"'", null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                if(cursorSelect.moveToFirst()) {
                    alertDialog.setTitle("The following student has been deleted");
                    String foundStudent = new String();
                    foundStudent = "ID: " + cursorSelect.getString(0) + " Name: " + cursorSelect.getString(1) + " Marks: " + cursorSelect.getString(2);
                    alertDialog.setMessage(foundStudent);
                    database.execSQL("DELETE FROM students WHERE student_id == '" + idEditText.getText().toString() +"'");
                } else {
                    alertDialog.setTitle("This student does not exist");
                }

                alertDialog.setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                clearTextFields();
                alertDialog.show();
            }
        });
    }

    public void clearTextFields() {
        idEditText.setText("");
        nameEditText.setText("");
        marksEditText.setText("");
    }

    public boolean isTextFieldsComplete() {
        if(idEditText.getText().toString().matches("") || nameEditText.getText().toString().matches("") || marksEditText.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Incomplete", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
