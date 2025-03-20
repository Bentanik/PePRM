package com.example.baiexam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ListView listViewAuthors;
    private List<String> authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Popup menu
        ImageView avatar = findViewById(R.id.imgAvatar);

        avatar.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, avatar);
            popup.getMenuInflater().inflate(R.menu.avatar_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_create_auth) {
                    showAddAuthorDialog();
                    return true;
                }else if (item.getItemId() == R.id.menu_create_book) {
                    showAddBookDialog();
                    return true;
                }else if (item.getItemId() == R.id.menu_logout) {
                    Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        });

        // List
        listViewAuthors = findViewById(R.id.listViewAuthors);

        // Danh sách tác giả
        authors = new ArrayList<>(Arrays.asList(
                "Nguyễn Nhật Ánh", "J.K. Rowling", "George Orwell",
                "Stephen King", "Haruki Murakami", "Agatha Christie"
        ));

        // Adapter để hiển thị danh sách
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, authors);
        listViewAuthors.setAdapter(adapter);

        // Bắt sự kiện click vào tác giả
        listViewAuthors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String authorName = authors.get(position);

                // Chuyển sang màn hình chi tiết tác giả
                Intent intent = new Intent(MainActivity.this, AuthorDetailActivity.class);
                intent.putExtra("author_name", authorName);
                intent.putExtra("author_email", "Email");
                intent.putExtra("author_address", "Địa chỉ");
                intent.putExtra("author_phone", "Số điện thoại");
                startActivity(intent);
            }
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

        AlertDialog dialog = builder.create();
        btnSubmitBook.setOnClickListener(v -> {
            String title = edtBookTitle.getText().toString();
            String publishDate = edtPublishDate.getText().toString();
            String category = edtCategory.getText().toString();
            String author = spinnerAuthors.getText().toString();

            Toast.makeText(MainActivity.this, "Đã lưu: " + title + " - " + author, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
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

        AlertDialog dialog = builder.create();
        btnSubmitAuthor.setOnClickListener(v -> {
            String name = edtAuthorName.getText().toString().trim();
            String email = edtAuthorEmail.getText().toString().trim();
            String address = edtAuthorAddress.getText().toString().trim();
            String phone = edtAuthorPhone.getText().toString().trim();

            Toast.makeText(MainActivity.this, "Tác giả đã thêm: " + name, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }




}