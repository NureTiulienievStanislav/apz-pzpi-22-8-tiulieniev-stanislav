#include <WiFi.h>
#include <PubSubClient.h>

// WiFi
const char* ssid = "Wokwi-GUEST";
const char* password = "";

// MQTT
const char* mqtt_server = "broker.hivemq.com";
const int mqtt_port = 1883;
const char* topic = "autocare/iot";

WiFiClient espClient;
PubSubClient client(espClient);

// Licence plate
const char* licence_plate = ""; 

// Ultrasonic sensor pins
const int pin_trig1 = 22; // Trig pin для переднього датчика
const int pin_echo1 = 23; // Echo pin для переднього датчика
const int pin_trig2 = 26; // Trig pin для заднього датчика
const int pin_echo2 = 27; // Echo pin для заднього датчика

void setup() {
  Serial.begin(115200);

  pinMode(pin_trig1, OUTPUT);
  pinMode(pin_echo1, INPUT);
  pinMode(pin_trig2, OUTPUT);
  pinMode(pin_echo2, INPUT);

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Підключення до WiFi...");
  }
  Serial.println("Підключено до WiFi");

  // Підключення до MQTT брокера
  client.setServer(mqtt_server, mqtt_port);
  while (!client.connected()) {
    if (client.connect("ESP32Client")) {
      Serial.println("Підключено до MQTT брокера");
    } else {
      delay(1000);
      Serial.println("Підключення до MQTT...");
    }
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  // Вимірювання відстані від переднього датчика
  int distance_front = measureDistance(pin_trig1, pin_echo1);

  // Вимірювання відстані від заднього датчика
  int distance_back = measureDistance(pin_trig2, pin_echo2);

  // Створення JSON повідомлення
  String message = "{\"distance_front\": " + String(distance_front) + 
                   ", \"distance_back\": " + String(distance_back) + 
                   ", \"licence_plate\": \"" + String(licence_plate) + "\"}";

  // Публікація JSON повідомлення на MQTT топік
  if (client.publish(topic, message.c_str())) {
    Serial.println("Опубліковано: " + message);
  } else {
    Serial.println("Не вдалося опублікувати повідомлення");
  }

  // Очікування перед наступним вимірюванням
  delay(5000); // 5 секунд
}

void reconnect() {
  // Підключення до MQTT брокера
  while (!client.connected()) {
    Serial.print("Підключення до MQTT...");
    if (client.connect("ESP32Client")) {
      Serial.println("Підключено до MQTT брокера");
    } else {
      Serial.print("Помилка підключення: ");
      Serial.println(client.state());
      delay(5000);  // Чекати 5 секунд перед повторною спробою
    }
  }
}


// Функція для вимірювання відстані за допомогою ультразвукового датчика
int measureDistance(int trigPin, int echoPin) {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  long duration = pulseIn(echoPin, HIGH); // Вимірювання тривалості імпульсу ехо
  int distance = duration * 0.034 / 2;   // Перетворення тривалості в відстань (в см)
  return distance;
}
