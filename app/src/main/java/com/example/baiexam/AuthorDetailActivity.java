package com.example.baiexam;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baiexam.adapter.ListViewAdapterForSach;
import com.example.baiexam.db.AppDatabase;
import com.example.baiexam.model.Sach;
import com.example.baiexam.model.TacGia;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class AuthorDetailActivity extends AppCompatActivity {
    private TextView txtAuthorName, txtEmail, txtAddress, txtPhone;
    private ListView listViewBooks;
    private List<Sach> bookList;
    private TacGia author;
    private int authorId;
    private ListViewAdapterForSach adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail);

        // Ánh xạ view
        txtAuthorName = findViewById(R.id.txtAuthorName);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        listViewBooks = findViewById(R.id.listViewBooks);


        // Get Author ID from Intent
        authorId = getIntent().getIntExtra("author_id", -1);
        if (authorId != -1) {
            loadAuthorDetails();
        }

        // Xử lý khi nhấn vào chỉnh sửa
        TextView txtEdit = findViewById(R.id.txtEdit);
        txtEdit.setOnClickListener(v -> showAddAuthorDialog());

        // Xử lý khi xóa
        TextView txtDelete = findViewById(R.id.txtDelete);
        txtDelete.setOnClickListener(v -> {
            confirmDelete();
        });

        // Xử lý khi nhấn vào sách
        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            Sach selectedBook = bookList.get(position);
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("BOOK_ID", selectedBook.getIdSach());
            startActivity(intent);
        });
    }

    // Load TacGia details and books
    private void loadAuthorDetails() {
        Executors.newSingleThreadExecutor().execute(() -> {
            author = AppDatabase.getInstance(this).tacGiaDao().loadTacGiaById(authorId);
            if (author != null) {
                runOnUiThread(() -> {
                    txtAuthorName.setText(author.getTenTacGia());
                    txtEmail.setText(author.getEmail());
                    txtAddress.setText(author.getDiaChi());
                    txtPhone.setText(author.getDienThoai());
                });
            }
        });
    }


    private void showAddAuthorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_author, null);
        builder.setView(dialogView);

        TextInputEditText edtAuthorName = dialogView.findViewById(R.id.edtAuthorName);
        TextInputEditText edtAuthorEmail = dialogView.findViewById(R.id.edtAuthorEmail);
        TextInputEditText edtAuthorAddress = dialogView.findViewById(R.id.edtAuthorAddress);
        TextInputEditText edtAuthorPhone = dialogView.findViewById(R.id.edtAuthorPhone);
        Button btnSubmitAuthor = dialogView.findViewById(R.id.btnSubmitAuthor);

        // Thêm dữ liệu vào
        TextView label = dialogView.findViewById(R.id.label);
        label.setText("Chỉnh sửa");

        if (author != null) {
            edtAuthorName.setText(author.getTenTacGia());
            edtAuthorEmail.setText(author.getEmail());
            edtAuthorAddress.setText(author.getDiaChi());
            edtAuthorPhone.setText(author.getDienThoai());
        }

        // Bắt sự kiện
        AlertDialog dialog = builder.create();
        btnSubmitAuthor.setOnClickListener(v -> {
            String name = edtAuthorName.getText().toString().trim();
            String email = edtAuthorEmail.getText().toString().trim();
            String address = edtAuthorAddress.getText().toString().trim();
            String phone = edtAuthorPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            author.setTenTacGia(name);
            author.setEmail(email);
            author.setDiaChi(address);
            author.setDienThoai(phone);

            // Update in Database
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(this).tacGiaDao().updateTacGia(author);
                runOnUiThread(() -> {
                    // Refresh UI
                    loadAuthorDetails();
                    dialog.dismiss();
                });
            });
        });

        dialog.show();
    }

    // Confirm Delete Dialog
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Author")
                .setMessage("Are you sure you want to delete this author?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteTacGia(dialog);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Close only the dialog
                .show();
    }

    // Delete TacGia from the database without closing activity
    private void deleteTacGia(DialogInterface dialog) {
        if (author != null) {
            new Thread(() -> {
                AppDatabase.getInstance(this).tacGiaDao().deleteTacGia(author);
                runOnUiThread(() -> finish()); // Close dialog after deletion
            }).start();
        }
    }

    public void loadListBooks(int id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            bookList = AppDatabase.getInstance(this).sachDao().getAllByTacGiaId(id);
            runOnUiThread(() -> {
                adapter = new ListViewAdapterForSach(this, bookList);
                listViewBooks.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        loadListBooks(authorId);
        super.onResume();
    }
}