# 🏠 Monitoring dan Kontrol IoT: Suhu & LED (ESP32 - Antares - Android)

## 🌟 Gambaran Umum Proyek

Proyek ini adalah sistem Internet of Things (IoT) yang berfungsi untuk **memantau suhu dan kelembapan** lingkungan menggunakan sensor **DHT22** serta memungkinkan **pengendalian lampu LED** dari jarak jauh melalui platform IoT **Antares**. 

* **ESP32** berfungsi sebagai perangkat IoT, mengirimkan data sensor (*Publish*) dan menerima perintah kontrol LED (*Subscribe*).
* **Aplikasi Android** berfungsi sebagai antarmuka pengguna (UI) untuk menampilkan data real-time dan mengirimkan perintah ON/OFF ke LED.



---

## 🛠️ Perangkat Keras dan Perangkat Lunak

| Komponen | Deskripsi |
| :--- | :--- |
| **Mikrokontroler** | ESP32 |
| **Sensor** | DHT22 (untuk Suhu & Kelembapan) |
| **Aktor** | LED (dihubungkan melalui resistor) |
| **Platform IoT** | Antares (sebagai *broker* data) |
| **Bahasa Pemrograman** | Arduino (untuk ESP32), Java (untuk Android) |
| **Libraries ESP32** | `WiFi.h`, `DHT.h`, `AntaresESPHTTP.h` |
| **Libraries Android** | Volley (untuk komunikasi HTTP) |

---

## 🔌 Skema Rangkaian (Contoh Umum)

**Koneksi ESP32:**

| Komponen | Pin ESP32 | Keterangan |
| :--- | :--- | :--- |
| **Sensor DHT22 Data Pin** | GPIO 4 | Pin input data sensor. |
| **LED Anode** | GPIO 5 | Pin output untuk kontrol LED. |
| **LED/DHT22 VCC** | 3.3V atau 5V | Sumber daya. |
| **LED/DHT22 GND** | GND | Ground. |

---

## 💻 Program Mikrokontroler (ESP32 - Arduino)

Kode ini menjalankan klien Antares menggunakan HTTP (via `AntaresESPHTTP.h`) untuk komunikasi data dua arah.

### ⚙️ Konfigurasi

Pastikan untuk mengganti kredensial Antares dan WiFi pada kode Anda.

```cpp

#define ACCESSKEY "5d22379e080b7127:8cbc3313738e51bf" // GANTI DENGAN ACCESS KEY ANDA
#define projectName "Tugas_IoT_Kelompok6"
#define SENSOR_DEVICE_NAME "MonitoringTemperature"
#define LED_DEVICE_NAME "Button"

const char* ssid = "Cari Gratisan yaa"; // GANTI DENGAN SSID WIFI
const char* password = "yaudahsambunginaja"; // GANTI DENGAN PASSWORD WIFI\

🔁 Alur Kerja UtamaWiFi: Klien terhubung ke WiFi. Jika terputus, akan mencoba menghubungkan ulang.Baca Sensor: Baca Suhu dan Kelembaban dari DHT22.PUBLISH (Setiap ~5 detik):Data Suhu dan Kelembapan dikirim sebagai Content Instance baru ke Device MonitoringTemperature.SUBSCRIBE (Setiap ~5 detik):Mengambil Content Instance terbaru dari Device Button.Mengecek nilai key status.Jika status = "1" $\rightarrow$ LED ON.Jika status = "0" $\rightarrow$ LED OFF.
