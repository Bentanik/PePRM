package com.example.baiexam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.baiexam.db.AppDatabase;
import com.example.baiexam.model.Sach;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class BookDetailActivity extends AppCompatActivity {

    private TextView txtAuthorName, txtBookCategory, txtBookTitle, txtPublishDate;
    private Sach book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Ánh xạ view
        txtAuthorName = findViewById(R.id.txtAuthorName);
        txtBookCategory = findViewById(R.id.txtBookCategory);
        txtBookTitle = findViewById(R.id.txtBookTitle);
        txtPublishDate = findViewById(R.id.txtPublishDate);

        int bookId = getIntent().getIntExtra("BOOK_ID", -1);
        if (bookId != -1) {
            loadBookDetails(bookId);
        }

        // Xử lý khi nhấn vào chỉnh sửa
        TextView txtEdit = findViewById(R.id.txtEdit);
        txtEdit.setOnClickListener(v -> showAddBookDialog());

        // Xử lý khi xóa
        TextView txtDelete = findViewById(R.id.txtDelete);
        txtDelete.setOnClickListener(v -> { confirmDelete();
        });
    }

    private void loadBookDetails(int bookId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            book = AppDatabase.getInstance(this).sachDao().loadSachById(bookId);
            String authorName = AppDatabase.getInstance(this).tacGiaDao().loadTacGiaById(book.getIdTacGia()).getTenTacGia();
            runOnUiThread(() -> {
                if (book != null) {
                    txtBookTitle.setText(book.getTenSach());
                    txtAuthorName.setText(authorName);
                    txtPublishDate.setText(book.getNgayXB());
                    txtBookCategory.setText(book.getTheLoai());
                }
            });
        });
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);
        builder.setView(dialogView);

        TextInputEditText edtBookTitle = dialogView.findViewById(R.id.edtBookTitle);
        TextInputEditText edtPublishDate = dialogView.findViewById(R.id.edtPublishDate);
        TextInputEditText edtCategory = dialogView.findViewById(R.id.edtCategory);
        Button btnSubmitBook = dialogView.findViewById(R.id.btnSubmitBook);

        Spinner spinnerAuthors = dialogView.findViewById(R.id.spinnerAuthors);
        List<String> singleItemList = new ArrayList<String>();
        singleItemList.add(txtAuthorName.getText().toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, singleItemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAuthors.setAdapter(adapter);
        spinnerAuthors.setEnabled(false); // Disables selection
        spinnerAuthors.setClickable(false); // Prevents clicks
        spinnerAuthors.setFocusable(false); // Avoids focus

        // Thêm dữ liệu vào
        TextView label = dialogView.findViewById(R.id.label);
        label.setText("Chỉnh sửa");
        // Map dữ liệu
        edtBookTitle.setText(txtBookTitle.getText().toString());
        edtPublishDate.setText(txtPublishDate.getText().toString());
        edtCategory.setText(txtBookCategory.getText().toString());

        // Gọi sự kiện
        AlertDialog dialog = builder.create();
        btnSubmitBook.setOnClickListener(v -> {
            String title = edtBookTitle.getText().toString();
            String publishDate = edtPublishDate.getText().toString();
            String category = edtCategory.getText().toString();
            if (title.isEmpty() || publishDate.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            book.setTenSach(title);
            book.setNgayXB(publishDate);
            book.setTheLoai(category);

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(this).sachDao().updateSach(book);
                runOnUiThread(() -> {
                    loadBookDetails(book.getIdSach());
                    dialog.dismiss();
                    Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                });
            });
        });

        dialog.show();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Book")
                .setMessage("Are you sure you want to delete this book?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBook())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteBook() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(this).sachDao().deleteSach(book);
            runOnUiThread(() -> {
                Toast.makeText(this, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}