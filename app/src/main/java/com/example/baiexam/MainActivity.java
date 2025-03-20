package com.example.baiexam;

import static com.example.baiexam.R.layout.activity_main;

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

import com.example.baiexam.adapter.ListViewAdapterForTacGia;
import com.example.baiexam.db.AppDatabase;
import com.example.baiexam.model.Sach;
import com.example.baiexam.model.TacGia;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ListViewAdapterForTacGia adapter;
    private ListView listViewAuthors;
    private List<TacGia> authorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(activity_main);
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
                } else if (item.getItemId() == R.id.menu_create_book) {
                    showAddBookDialog();
                    return true;
                } else if (item.getItemId() == R.id.menu_logout) {
                    Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });

            popup.show();
        });

        // List
        listViewAuthors = findViewById(R.id.listViewAuthors);

        // Thêm data lần đầu tiên
//        Executors.newSingleThreadExecutor().execute(() -> {
//            AppDatabase db = AppDatabase.getInstance(this);
//
//            // Insert Authors (TacGia)
//            TacGia author1 = new TacGia("John Smith", "john.smith@example.com", "New York, USA", "123-456-7890");
//            TacGia author2 = new TacGia("Jane Doe", "jane.doe@example.com", "Los Angeles, USA", "987-654-3210");
//            TacGia author3 = new TacGia("Michael Johnson", "michael.j@example.com", "Chicago, USA", "555-123-6789");
//
//            db.tacGiaDao().insertTacGia(author1);
//            db.tacGiaDao().insertTacGia(author2);
//            db.tacGiaDao().insertTacGia(author3);
//
//            // Retrieve all authors to get their IDs
//            List<TacGia> tacGiaList = db.tacGiaDao().getAllTacGia();
//
//            // Insert Books (Sach) using author IDs
//            if (!tacGiaList.isEmpty()) {
//                db.sachDao().insertSach(new Sach("Android Development", "2023-01-10", "Technology", tacGiaList.get(0).getIdTacGia()));
//                db.sachDao().insertSach(new Sach("Machine Learning Basics", "2022-05-20", "Science", tacGiaList.get(1).getIdTacGia()));
//                db.sachDao().insertSach(new Sach("Fictional World", "2021-07-15", "Fiction", tacGiaList.get(2).getIdTacGia()));
//            }
//        });

        // Bắt sự kiện click vào tác giả
        listViewAuthors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TacGia selectedAuthor = authorsList.get(position);
                Intent intent = new Intent(MainActivity.this, AuthorDetailActivity.class);
                intent.putExtra("author_id", selectedAuthor.getIdTacGia());  // Pass only the ID
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
        Spinner spinnerAuthors = dialogView.findViewById(R.id.spinnerAuthors);
        Button btnSubmitBook = dialogView.findViewById(R.id.btnSubmitBook);

        // Load authors into Spinner
        Executors.newSingleThreadExecutor().execute(() -> {
            List<TacGia> tacGiaList = AppDatabase.getInstance(this).tacGiaDao().getAllTacGia();
            List<String> authorNames = new ArrayList<>();
            for (TacGia tacGia : tacGiaList) {
                authorNames.add(tacGia.getTenTacGia());
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, authorNames);
                spinnerAuthors.setAdapter(adapter);
            });
        });

        AlertDialog dialog = builder.create();
        btnSubmitBook.setOnClickListener(v -> {
            String title = edtBookTitle.getText().toString();
            String publishDate = edtPublishDate.getText().toString();
            String category = edtCategory.getText().toString();
            int selectedPosition = spinnerAuthors.getSelectedItemPosition();

            if (title.isEmpty() || publishDate.isEmpty() || category.isEmpty() || selectedPosition == -1) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                TacGia selectedAuthor = authorsList.get(selectedPosition);
                Sach sach = new Sach(title, publishDate, category, selectedAuthor.getIdTacGia());
                AppDatabase.getInstance(this).sachDao().insertSach(sach);
                runOnUiThread(() -> {
                    loadListAuthors();
                    Toast.makeText(this, "Add Book Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });
        });

        dialog.show();
    }

    private void showAddAuthorDialog() {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_author, null);
        builder.setView(dialogView);

        // Find View By Id
        TextInputEditText edtAuthorName = dialogView.findViewById(R.id.edtAuthorName);
        TextInputEditText edtAuthorEmail = dialogView.findViewById(R.id.edtAuthorEmail);
        TextInputEditText edtAuthorAddress = dialogView.findViewById(R.id.edtAuthorAddress);
        TextInputEditText edtAuthorPhone = dialogView.findViewById(R.id.edtAuthorPhone);
        Button btnSubmitAuthor = dialogView.findViewById(R.id.btnSubmitAuthor);

        AlertDialog dialog = builder.create();

        // Bắt sự kiện Submit
        btnSubmitAuthor.setOnClickListener(v -> {
            String name = edtAuthorName.getText().toString().trim();
            String email = edtAuthorEmail.getText().toString().trim();
            String address = edtAuthorAddress.getText().toString().trim();
            String phone = edtAuthorPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            TacGia tacGia = new TacGia();
            tacGia.setTenTacGia(name);
            tacGia.setEmail(email);
            tacGia.setDiaChi(address);
            tacGia.setDienThoai(phone);

            // Add to Database
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(this).tacGiaDao().insertTacGia(tacGia);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Add author successfully", Toast.LENGTH_SHORT).show();
                    loadListAuthors();
                    dialog.dismiss();
                });
            });
        });
        dialog.show();
    }

    public void loadListAuthors() {
        Executors.newSingleThreadExecutor().execute(() -> {
            authorsList = AppDatabase.getInstance(this).tacGiaDao().getAllTacGia();
            runOnUiThread(() -> {
                adapter = new ListViewAdapterForTacGia(this, authorsList);
                listViewAuthors.setAdapter(adapter);
            });
        });
    }

    @Override
    protected void onResume() {
        loadListAuthors();
        super.onResume();
    }
}