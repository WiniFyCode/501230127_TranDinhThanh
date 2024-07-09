package com.example.a501230127_trandinhthanh;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a501230127_trandinhthanh.Adapter.TourAdapter;
import com.example.a501230127_trandinhthanh.Model.Tour;
import com.example.a501230127_trandinhthanh.SQLite.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lvTour;
    EditText edtMaTour, edtTenTour, edtGia;
    Button btnThem, btnSua, btnXoa;
    ArrayList<Tour> duLieu = new ArrayList<>();
    TourAdapter tourAdapter;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Thay thế bằng layout activity_main của bạn

        // Ánh xạ các thành phần giao diện
        lvTour = findViewById(R.id.lvTour);
        edtMaTour = findViewById(R.id.edtTenTour);
        edtTenTour = findViewById(R.id.edtMaTour);
        edtGia = findViewById(R.id.edtGia);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);

        // Đăng ký sự kiện cho các nút
        btnThem.setOnClickListener(this);
        btnSua.setOnClickListener(this);
        btnXoa.setOnClickListener(this);

        // Mở cơ sở dữ liệu
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        // Khởi tạo adapter và gán cho ListView
        tourAdapter = new TourAdapter(this, R.layout.item_listview, duLieu); // Thay thế bằng layout item của bạn
        lvTour.setAdapter(tourAdapter);

        // Tải dữ liệu vào ListView
        loadListView();

        // Đăng ký sự kiện click cho ListView
        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tour tour = duLieu.get(position);
                edtMaTour.setText(tour.MaTour);
                edtTenTour.setText(tour.TenTour);
                edtGia.setText(String.valueOf(tour.Gia));
            }
        });
    }

    private void loadListView() {
        duLieu.clear();
        try (Cursor cursor = db.query("TOUR", null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                duLieu.add(new Tour(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2)
                ));
            }
        }
        tourAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        ContentValues contentValues = new ContentValues();

        try {
            if (id == R.id.btnThem) {
                String maTour = edtMaTour.getText().toString();
                String tenTour = edtTenTour.getText().toString();
                int gia = Integer.parseInt(edtGia.getText().toString());

                if (maTour.isEmpty() || tenTour.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor = db.rawQuery("SELECT * FROM TOUR WHERE MaTour = ?", new String[]{maTour});
                if (cursor.getCount() > 0) {
                    Toast.makeText(this, "Mã Tour đã tồn tại!", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                contentValues.put("MaTour", maTour);
                contentValues.put("TenTour", tenTour);
                contentValues.put("Gia", gia);
                db.insert("TOUR", null, contentValues);
                loadListView();
                clearEditText();
            } else if (id == R.id.btnSua) {
                String maTour = edtMaTour.getText().toString();
                String tenTour = edtTenTour.getText().toString();
                int gia = Integer.parseInt(edtGia.getText().toString());

                if (maTour.isEmpty() || tenTour.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                contentValues.put("TenTour", tenTour);
                contentValues.put("Gia", gia);
                db.update("TOUR", contentValues, "MaTour=?", new String[]{maTour});
                loadListView();
                clearEditText();
            } else if (id == R.id.btnXoa) {
                String maTour = edtMaTour.getText().toString();

                if (maTour.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập Mã Tour để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa tour có mã " + maTour + " này?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            db.delete("TOUR", "MaTour=?", new String[]{maTour});
                            loadListView();
                            clearEditText();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin ", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditText() {
        edtMaTour.setText("");
        edtTenTour.setText("");
        edtGia.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}