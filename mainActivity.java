package com.example.iot_kelompok6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Import Library Volley
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // === KONFIGURASI ANTARES (SESUAIKAN DISINI) ===
    final String ACCESS_KEY = "5d22379e080b7127:8cbc3313738e51bf";
    final String APP_NAME = "Tugas_IoT_Kelompok6";
    final String DEVICE_SENSOR = "MonitoringTemperature";
    final String DEVICE_LED = "Button";
    final String BASE_URL = "https://platform.antares.id:8443/~/antares-cse/antares-id/";

    // Deklarasi tvCahaya DIHAPUS karena tidak digunakan
    TextView tvSuhu, tvKelembaban;
    Button btnRefresh, btnLedOn, btnLedOff;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hubungkan variabel dengan tampilan (Layout)
        tvSuhu = findViewById(R.id.tvSuhu);
        tvKelembaban = findViewById(R.id.tvKelembaban);
        // Baris untuk tvCahaya dihapus

        btnRefresh = findViewById(R.id.btnRefresh);
        btnLedOn = findViewById(R.id.btnLedOn);
        btnLedOff = findViewById(R.id.btnLedOff);

        requestQueue = Volley.newRequestQueue(this);

        // Aksi saat tombol ditekan
        btnRefresh.setOnClickListener(v -> getDataSensor());
        btnLedOn.setOnClickListener(v -> kirimPerintahLed("1"));
        btnLedOff.setOnClickListener(v -> kirimPerintahLed("0"));

        // Ambil data pertama kali saat aplikasi dibuka
        getDataSensor();
    }

    // Fungsi Ambil Data Sensor (GET)
    private void getDataSensor() {
        String url = BASE_URL + APP_NAME + "/" + DEVICE_SENSOR + "/la";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject m2mCin = response.getJSONObject("m2m:cin");
                        String conString = m2mCin.getString("con");
                        JSONObject dataSensor = new JSONObject(conString);

                        int temp = dataSensor.getInt("Suhu");
                        int hum = dataSensor.getInt("Kelembapan");

                        tvSuhu.setText("Suhu: " + temp + " °C");
                        tvKelembaban.setText("Kelembaban: " + hum + " %");
                        Toast.makeText(MainActivity.this, "Data Terupdate", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Format Data Salah", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Gagal Koneksi Antares", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-M2M-Origin", ACCESS_KEY);
                headers.put("Content-Type", "application/json;ty=4");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    // Fungsi Kirim Perintah LED (POST)
    private void kirimPerintahLed(String status) {
        String url = BASE_URL + APP_NAME + "/" + DEVICE_LED;
        // Format payload JSON manual
        String rawPayload = "{\"m2m:cin\": {\"con\": \"{\\\"status\\\":\\\"" + status + "\\\"}\"}}";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(MainActivity.this, "Sukses: Lampu " + (status.equals("1") ? "ON" : "OFF"), Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MainActivity.this, "Gagal Kirim Perintah", Toast.LENGTH_SHORT).show()) {
            @Override
            public byte[] getBody() { return rawPayload.getBytes(); }
            @Override
            public String getBodyContentType() { return "application/json;ty=4"; }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-M2M-Origin", ACCESS_KEY);
                headers.put("Content-Type", "application/json;ty=4");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
