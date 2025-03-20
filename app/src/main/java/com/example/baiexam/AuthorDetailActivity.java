package com.example.baiexam;

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

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorDetailActivity extends AppCompatActivity {
    private TextView txtAuthorName, txtEmail, txtAddress, txtPhone;
    private ListView listViewBooks;
    private List<Map<String, String>> bookList;
    private SimpleAdapter bookAdapter;

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

        // Nhận dữ liệu từ Intent
        String authorName = getIntent().getStringExtra("author_name");
        String email = getIntent().getStringExtra("author_email");
        String address = getIntent().getStringExtra("author_address");
        String phone = getIntent().getStringExtra("author_phone");

        // Hiển thị thông tin tác giả
        txtAuthorName.setText(authorName);
        txtEmail.setText(email);
        txtAddress.setText(address);
        txtPhone.setText(phone);

        // Danh sách sách (dữ liệu giả lập)
        bookList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, String> book = new HashMap<>();
            book.put("title", "Sách " + i + " của " + authorName);
            bookList.add(book);
        }

        // Cấu hình Adapter cho ListView
        bookAdapter = new SimpleAdapter(
                this,
                bookList,
                R.layout.list_item_book,
                new String[]{"title"},
                new int[]{R.id.txtBookTitle}
        );

        listViewBooks.setAdapter(bookAdapter);

        // Xử lý khi nhấn vào chỉnh sửa
        TextView txtEdit = findViewById(R.id.txtEdit);
        txtEdit.setOnClickListener(v -> showAddAuthorDialog());

        // Xử lý khi xóa
        TextView txtDelete = findViewById(R.id.txtDelete);
        txtDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // TODO: Gọi hàm xóa tác giả ở đây
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Xử lý khi nhấn vào sách
        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            String bookTitle = bookList.get(position).get("title");
            Intent intent = new Intent(AuthorDetailActivity.this, BookDetailActivity.class);
            intent.putExtra("book_id", bookTitle);
            startActivity(intent);
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
        // Map dữ liệu
        edtAuthorName.setText(txtAuthorName.getText().toString());
        edtAuthorEmail.setText(txtEmail.getText().toString());
        edtAuthorAddress.setText(txtAddress.getText().toString());
        edtAuthorPhone.setText(txtPhone.getText().toString());

        // Bắt sự kiện
        AlertDialog dialog = builder.create();
        btnSubmitAuthor.setOnClickListener(v -> {
            String name = edtAuthorName.getText().toString().trim();
            String email = edtAuthorEmail.getText().toString().trim();
            String address = edtAuthorAddress.getText().toString().trim();
            String phone = edtAuthorPhone.getText().toString().trim();

            Toast.makeText(this, "Tác giả đã được chỉnh sửa: " + name, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}