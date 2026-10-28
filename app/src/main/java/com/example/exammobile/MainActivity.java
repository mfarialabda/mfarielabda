package com.example.exammobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String[] MAJORS = {"Informatics", "Information Systems"};

    private EditText etName, etStudentId;
    private Spinner spMajor;
    private Button btnAdd, btnEdit, btnDelete, btnView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        spMajor = findViewById(R.id.spMajor);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        spMajor.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, MAJORS));

        btnAdd.setOnClickListener(v -> {
            if (!validate(true)) return;
            boolean ok = db.insertStudent(getName(), getId(), getMajor());
            toast(ok ? "Data berhasil ditambahkan" : "Gagal: Student ID sudah ada");
            if (ok) clearForm();
        });

        btnEdit.setOnClickListener(v -> {
            if (!validate(true)) return;
            boolean ok = db.updateStudent(getId(), getName(), getMajor());
            toast(ok ? "Data berhasil diubah" : "Student ID tidak ditemukan");
            if (ok) clearForm();
        });

        btnDelete.setOnClickListener(v -> {
            if (!validate(false)) return;
            boolean ok = db.deleteStudent(getId());
            toast(ok ? "Data berhasil dihapus" : "Student ID tidak ditemukan");
            if (ok) clearForm();
        });

        btnView.setOnClickListener(v -> {
            String data = db.getAllAsText();
            if (data.isEmpty()) data = "Belum ada data.";
            new AlertDialog.Builder(this)
                    .setTitle("Data Mahasiswa")
                    .setMessage(data)
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    private String getName() { return etName.getText().toString().trim(); }
    private String getId() { return etStudentId.getText().toString().trim(); }
    private String getMajor() { return spMajor.getSelectedItem().toString(); }

    // needName = true untuk Add/Edit, false untuk Delete (cukup Student ID)
    private boolean validate(boolean needName) {
        if (needName && TextUtils.isEmpty(getName())) {
            etName.setError("Nama wajib diisi");
            return false;
        }
        if (TextUtils.isEmpty(getId())) {
            etStudentId.setError("Student ID wajib diisi");
            return false;
        }
        return true;
    }

    private void clearForm() {
        etName.setText("");
        etStudentId.setText("");
        spMajor.setSelection(0);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}