package com.example.baiexam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    private TextView txtBookId, txtAuthorName, txtBookCategory, txtBookTitle, txtPublishDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Ánh xạ view
        txtBookId = findViewById(R.id.txtBookId);
        txtAuthorName = findViewById(R.id.txtAuthorName);
        txtBookCategory = findViewById(R.id.txtBookCategory);
        txtBookTitle = findViewById(R.id.txtBookTitle);
        txtPublishDate = findViewById(R.id.txtPublishDate);

        // Nhận dữ liệu từ Intent
        String bookId = getIntent().getStringExtra("book_id");
        // Gán dự liệu
        txtBookId.setText(bookId);
        txtAuthorName.setText("Tác giả");
        txtBookCategory.setText("Sách");
        txtBookTitle.setText("Tiêu đề");
        txtPublishDate.setText("Ngày xuất bản");

        // Xử lý khi nhấn vào chỉnh sửa
        TextView txtEdit = findViewById(R.id.txtEdit);
        txtEdit.setOnClickListener(v -> showAddBookDialog());

        // Xử lý khi xóa
        TextView txtDelete = findViewById(R.id.txtDelete);
        txtDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // TODO: Gọi hàm xóa ở đây
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
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
        AutoCompleteTextView spinnerAuthors = dialogView.findViewById(R.id.spinnerAuthors);
        Button btnSubmitBook = dialogView.findViewById(R.id.btnSubmitBook);

        // Danh sách tác giả
        List<String> authors = Arrays.asList("Tác giả A", "Tác giả B", "Tác giả C");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, authors);
        spinnerAuthors.setAdapter(adapter);

        // Thêm dữ liệu vào
        TextView label = dialogView.findViewById(R.id.label);
        label.setText("Chỉnh sửa");
        // Map dữ liệu
        edtBookTitle.setText(txtBookTitle.getText().toString());
        edtPublishDate.setText(txtPublishDate.getText().toString());
        edtCategory.setText(txtBookCategory.getText().toString());
        spinnerAuthors.setText(authors.get(0), false);

        // Gọi sự kiện
        AlertDialog dialog = builder.create();
        btnSubmitBook.setOnClickListener(v -> {
            String title = edtBookTitle.getText().toString();
            String publishDate = edtPublishDate.getText().toString();
            String category = edtCategory.getText().toString();
            String author = spinnerAuthors.getText().toString();

            Toast.makeText(this, "Đã lưu: " + title + " - " + author, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}