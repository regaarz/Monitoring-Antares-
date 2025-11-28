#include <WiFi.h>
#include <DHT.h>
#include <AntaresESPHTTP.h>

#define DHTPIN 4
#define DHTTYPE DHT22
DHT dht(DHTPIN, DHTTYPE);
#define LED_PIN 5 

#define ACCESSKEY "5d22379e080b7127:8cbc3313738e51bf"
#define projectName "Tugas_IoT_Kelompok6"
#define SENSOR_DEVICE_NAME "MonitoringTemperature" 
#define LED_DEVICE_NAME "Button" 

AntaresESPHTTP antares(ACCESSKEY);

const char* ssid = "Cari Gratisan yaa";
const char* password = "yaudahsambunginaja";

void setup_wifi() {
    Serial.print("Connecting to WiFi");
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(700);
        Serial.print(".");
    }

    Serial.println("\nWiFi Connected!");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());
}

void setup() {
    Serial.begin(115200);
    dht.begin();
    pinMode(LED_PIN, OUTPUT);
    digitalWrite(LED_PIN, LOW); 
    setup_wifi();
    antares.setDebug(true); 
    Serial.println("System Ready (Sensor Publisher & LED Subscriber)");
}

void loop() {
    if (WiFi.status() != WL_CONNECTED) {
        Serial.println("WiFi terputus. Mencoba menghubungkan ulang...");
        setup_wifi();
        return;
    }
    float temperature = dht.readTemperature();
    float humidity = dht.readHumidity();

    if (isnan(temperature) || isnan(humidity)) {
        Serial.println("Error membaca DHT22! Mencoba lagi...");
        delay(1000);
        return;
    }
    Serial.print("\n[PUBLISH] Suhu: ");
    Serial.print(temperature);
    Serial.print(" °C | Kelembapan: ");
    Serial.println(humidity);

    antares.add("Suhu", String(temperature, 2));
    antares.add("Kelembapan", String(humidity, 2));

    antares.send(projectName, SENSOR_DEVICE_NAME);
    Serial.println("Percobaan kirim data sensor selesai.");
    delay(500); 

    Serial.print("[SUBSCRIBE] Mengambil perintah dari Antares...");

    antares.get(projectName, LED_DEVICE_NAME);

    if (antares.getSuccess()) {
        Serial.println(" Data berhasil diambil.");
        
        String led_status_str = antares.getString("status");

        if (led_status_str.equals("1") || led_status_str.equals("0")) {
            Serial.print(" Perintah diterima: ");
            Serial.println(led_status_str);

            if (led_status_str.equals("1")) {
                digitalWrite(LED_PIN, HIGH);
                Serial.println("-> LED ON");
            } else { // status '0'
                digitalWrite(LED_PIN, LOW);
                Serial.println("-> LED OFF");
            }
        } else {
            Serial.print(" Gagal. Data 'status' tidak valid: '");
            Serial.print(led_status_str); 
            Serial.println("'");
        }
    } else {
        Serial.println(" GAGAL Koneksi/Ambil Data dari Antares.");
    }
    delay(4500); 
}
