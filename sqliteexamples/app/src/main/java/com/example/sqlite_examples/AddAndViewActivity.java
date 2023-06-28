package com.example.sqlite_examples;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlite_examples.helper.Helper;
import com.example.sqlite_examples.model.Friend;
import com.example.sqlite_examples.service.FriendServiceSQLiteViaContentValues;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AddAndViewActivity extends AppCompatActivity {
    private Friend temp = null;
    private TextInputEditText tieID, tieName, tieBirthday, tieAddress, tiePhone;
    private Button btClear, btSave;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);

        tieID = findViewById(R.id.tie_id);
        tieName = findViewById(R.id.tie_name);
        tieBirthday = findViewById(R.id.tie_birthday);
        tieAddress = findViewById(R.id.tie_address);
        tiePhone = findViewById(R.id.tie_phone);

        btClear = findViewById(R.id.bt_clear);
        btSave = findViewById(R.id.bt_save);

        btClear.setOnClickListener(this::clearForm);
        btSave.setOnClickListener(this::saveForm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveForm(View view) {
        if (Objects.requireNonNull(tieName.getText()).toString().isEmpty()) {
            Toast.makeText(this, "Name cannot empties", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.ADD_MODE) {
            Friend friend = new Friend(
                    Objects.requireNonNull(tieName.getText()).toString(),
                    LocalDate.parse(Objects.requireNonNull(tieBirthday.getText()).toString()),
                    Objects.requireNonNull(tieAddress.getText()).toString(),
                    Objects.requireNonNull(tiePhone.getText()).toString());
            try (FriendServiceSQLiteViaContentValues db = new FriendServiceSQLiteViaContentValues(this)) {
                db.insert(friend);
                super.onBackPressed();
            }
        } else if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.EDIT_MODE) {
            Friend friend = new Friend(
                    Integer.parseInt(Objects.requireNonNull(tieID.getText()).toString()),
                    Objects.requireNonNull(tieName.getText()).toString(),
                    LocalDate.parse(Objects.requireNonNull(tieBirthday.getText()).toString()),
                    Objects.requireNonNull(tieAddress.getText()).toString(),
                    Objects.requireNonNull(tiePhone.getText()).toString());

            try (FriendServiceSQLiteViaContentValues db = new FriendServiceSQLiteViaContentValues(this)) {
                db.update(friend);
                super.onBackPressed();
            }
        }
    }

    private void clearForm(View view) {
        tieID.setText("");
        tieName.setText("");
        tieBirthday.setText("");
        tieAddress.setText("");
        tiePhone.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        //add mode
        if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.ADD_MODE) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("ADD MODE");
            tieID.setFocusable(false);
        } //edit mode
        else if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.EDIT_MODE) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("EDIT MODE");
            tieID.setFocusable(false);
            btClear.setVisibility(View.GONE);

            Friend friend = (Friend) getIntent().getSerializableExtra(Helper.TAG_FRIEND);
            temp = friend;

            tieID.setText(String.valueOf(friend.getId()));
            tieName.setText(friend.getName());
            tieBirthday.setText(friend.getBirthday().format(DateTimeFormatter.ISO_DATE));
            tieAddress.setText(friend.getAddress());
            tiePhone.setText(friend.getPhone());
        } //view mode
        else if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.VIEW_MODE) {
            tieID.setFocusable(false);
            tieName.setFocusable(false);
            tieBirthday.setFocusable(false);
            tieAddress.setFocusable(false);
            tiePhone.setFocusable(false);
            btClear.setVisibility(View.GONE);
            btSave.setVisibility(View.GONE);

            Friend friend = (Friend) getIntent().getSerializableExtra(Helper.TAG_FRIEND);
            tieID.setText(String.valueOf(friend.getId()));
            tieName.setText(friend.getName());
            tieBirthday.setText(friend.getBirthday().format(DateTimeFormatter.ISO_DATE));
            tieAddress.setText(friend.getAddress());
            tiePhone.setText(friend.getPhone());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        if (getIntent().getIntExtra(Helper.ACTIVITY_MODE, -1) == Helper.EDIT_MODE) {
            if (Objects.requireNonNull(tieName.getText()).toString().isEmpty()) {
                Toast.makeText(this, "Name cannot empties!", Toast.LENGTH_SHORT).show();
                return;
            }

            Friend friend = new Friend(
                    Integer.parseInt(Objects.requireNonNull(tieID.getText()).toString()),
                    tieName.getText().toString(),
                    LocalDate.parse(Objects.requireNonNull(tieBirthday.getText()).toString()),
                    Objects.requireNonNull(tieAddress.getText()).toString(),
                    Objects.requireNonNull(tiePhone.getText()).toString()
            );

            if (!temp.equals(friend)) {
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setTitle("Telah terjadi perubahan data, simpan?")
                        .setNegativeButton("No", (dialog, which) -> super.onBackPressed())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try (FriendServiceSQLiteViaContentValues db = new FriendServiceSQLiteViaContentValues(this)) {
                                db.update(friend);
                                super.onBackPressed();
                            }
                        })
                        .show();
            } else super.onBackPressed();
        } else super.onBackPressed();
    }
}