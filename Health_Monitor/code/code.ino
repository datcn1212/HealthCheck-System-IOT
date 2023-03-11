#include <ArduinoJson.h>
#include <WiFi.h>
#include <PubSubClient.h>
#define PUB "esp32/on"
// WiFi
const char *ssid = "Canada"; // Enter your WiFi name
const char *password = "caonhudat2";  // Enter WiFi password

// MQTT Broker
const char *mqtt_broker = "broker.hivemq.com";
const char *topic = "dht11";
const char *mqtt_username = "manh";
const char *mqtt_password = "020601";
const int mqtt_port = 1883;

String deviceId = "E123458";
int heartRate;
int bodyTemperature;
int systolic;
int diastolic;

char str_sensor[10];
char str_millis[20];
double epochseconds = 0;
double epochmilliseconds = 0;
double current_millis = 0;
double current_millis_at_sensordata = 0;
double timestampp = 0;
char payload[10000];

bool requestData = false;

WiFiClient espClient;
PubSubClient client(espClient);

#define SENSORPIN A0

int UpperThreshold = 2400;
int LowerThreshold = 2350;
int reading = 0;
float BPM = 0.0;
bool IgnoreReading = false;
bool FirstPulseDetected = false;
unsigned long FirstPulseTime = 0;
unsigned long SecondPulseTime = 0;
unsigned long PulseInterval = 0;

void callback(char *topic, byte *payload, unsigned int length);

void publishMessage()
{
    heartRate = random(70,80);
    bodyTemperature = random(36,37);
    systolic = random(115,125);
    diastolic = random(75,85);

    Serial.println();
    Serial.print(F("Id:  "));
    Serial.print(deviceId);
    Serial.print(F("  Heart rate: "));
    Serial.print(heartRate);
    Serial.print(F("  Body temperature: "));
    Serial.print(bodyTemperature);
    Serial.print(F("   Systolic: "));
    Serial.print(systolic);
    Serial.print(F("   Diastolic: "));
    Serial.print(diastolic);
    Serial.println();

  StaticJsonDocument<200> doc;
  doc["Id"] = deviceId;
  doc["heartRate"] = heartRate;
  doc["bodyTemperature"] = bodyTemperature;
  doc["systolic"] = systolic;
  doc["diastolic"] = diastolic;
  char jsonBuffer[512];
  serializeJson(doc, jsonBuffer); // print to client
 
  client.publish(topic, jsonBuffer);
}

void setup() {
    // Set software serial baud to 115200;
    Serial.begin(115200);

    pinMode(SENSORPIN, INPUT);
    // connecting to a WiFi network
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi..");
    }
    Serial.println("Connected to the WiFi network");
    //connecting to a mqtt broker
    client.setServer(mqtt_broker, mqtt_port);
    client.setCallback(callback);
    while (!client.connected()) {
        String client_id = "esp32-client-";
        client_id += String(WiFi.macAddress());
        Serial.printf("The client %s connects to the public mqtt broker\n", client_id.c_str());
        if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
            Serial.println("Public emqx mqtt broker connected");
        } else {
            Serial.print("failed with state ");
            Serial.print(client.state());
            delay(2000);
        }
    }

    // publish and subscribe
    //client.publish(topic, "{\"Id\": \"E123458\",\"heartRate\": \"88\",\"bodyTemperature\": \"37.012\",\"systolic\": \"110.382\", \"diastolic\": \"89.884\"}");
    publishMessage();
    //client.subscribe("esp32/on");
}

void callback(char *topic, byte *payload, unsigned int length) {
    char status[20];
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message:");
    for (int i = 0; i < length; i++) {
        status[i] = payload[i];
    }
    Serial.println(status);
    Serial.println();
    if(String(topic) == PUB){
        if(String(status) == "send"){
            requestData = true;
        }
    }
    Serial.println("-----------------------");
}

void loop() {
    for (int i = 1; i <= 3; i++)
    {
        float sensor = analogRead(SENSORPIN);
        dtostrf(sensor, 4, 2, str_sensor);
        sprintf(payload, "%s{\"value\":", payload); // Adds the value
        sprintf(payload, "%s %s,", payload, str_sensor); // Adds the value
        current_millis_at_sensordata = millis();
        timestampp = epochmilliseconds + (current_millis_at_sensordata - current_millis);
        dtostrf(timestampp, 10, 0, str_millis);
        sprintf(payload, "%s \"timestamp\": %s},", payload, str_millis); // Adds the value
        delay(150);
    }
    float sensor = analogRead(SENSORPIN);
    dtostrf(sensor, 4, 2, str_sensor);
    current_millis_at_sensordata = millis();
    timestampp = epochmilliseconds + (current_millis_at_sensordata - current_millis);
    dtostrf(timestampp, 10, 0, str_millis);
    sprintf(payload, "%s{\"value\":%s, \"timestamp\": %s}]}", payload, str_sensor, str_millis);
    //client.publish(topic, payload);
    Serial.println();
    Serial.println(payload);
    Serial.println();

    if(sensor > UpperThreshold && IgnoreReading == false){
    if(FirstPulseDetected == false){
      FirstPulseTime = millis();
      FirstPulseDetected = true;
    }
    else{
      SecondPulseTime = millis();
      PulseInterval = SecondPulseTime - FirstPulseTime;
      FirstPulseTime = SecondPulseTime;
      IgnoreReading = true;
    }
    }

    if(sensor < LowerThreshold){
    IgnoreReading = false;
    }  

    BPM = (1.0/PulseInterval) * 60.0 * 1000;

    Serial.print(sensor);
    Serial.print("\t");
    Serial.print(PulseInterval);
    Serial.print("\t");
    Serial.print(BPM);
    Serial.println(" BPM");

    //client.subscribe("esp32/on");
    publishMessage();
    client.loop();
    delay(10000);
    // if(requestData){
    //     client.publish(topic, "{\"Id\": \"E123458\",\"heartRate\": \"999\",\"bodyTemperature\": \"101\",\"bloodPressure\": \"102\"}");
    //     requestData = false;
    // }
}
