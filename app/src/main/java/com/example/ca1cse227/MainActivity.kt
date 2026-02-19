package com.example.ca1cse227

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().getReference("Students")

        val etName = findViewById<EditText>(R.id.etName)
        val etRollNumber = findViewById<EditText>(R.id.etRollNumber)
        val etMarks = findViewById<EditText>(R.id.etMarks)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnRead = findViewById<Button>(R.id.btnRead)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val rollNumber = etRollNumber.text.toString()
            val marks = etMarks.text.toString()

            if (name.isNotEmpty() && rollNumber.isNotEmpty() && marks.isNotEmpty()) {
                val student = Student(rollNumber, name, rollNumber, marks)
                database.child(rollNumber).setValue(student).addOnSuccessListener {
                    etName.text.clear()
                    etRollNumber.text.clear()
                    etMarks.text.clear()
                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnRead.setOnClickListener {
            val rollNumber = etRollNumber.text.toString()
            if (rollNumber.isNotEmpty()) {
                database.child(rollNumber).get().addOnSuccessListener {
                    if (it.exists()) {
                        val name = it.child("name").value
                        val marks = it.child("marks").value
                        val roll = it.child("rollNumber").value
                        tvResult.text = "Name: $name\nRoll No: $roll\nMarks: $marks"
                        Toast.makeText(this, "Successfully Read", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Student does not exist", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to Read", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter Roll Number to read", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString()
            val rollNumber = etRollNumber.text.toString()
            val marks = etMarks.text.toString()

            if (rollNumber.isNotEmpty()) {
                val studentUpdates = mutableMapOf<String, Any>()
                if (name.isNotEmpty()) studentUpdates["name"] = name
                if (marks.isNotEmpty()) studentUpdates["marks"] = marks

                if (studentUpdates.isNotEmpty()) {
                    database.child(rollNumber).updateChildren(studentUpdates).addOnSuccessListener {
                        etName.text.clear()
                        etRollNumber.text.clear()
                        etMarks.text.clear()
                        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter Roll Number to update", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            val rollNumber = etRollNumber.text.toString()
            if (rollNumber.isNotEmpty()) {
                database.child(rollNumber).removeValue().addOnSuccessListener {
                    etRollNumber.text.clear()
                    tvResult.text = ""
                    Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter Roll Number to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
