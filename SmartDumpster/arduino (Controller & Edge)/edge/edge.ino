#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>
#include <ArduinoJson.h>
#include "Led.h"
#include "Potentiometer.h"

#define LED_AVAIL_PIN 4 //esp - D2 <=> GPIO4
#define LED_NOTAVAIL_PIN 5 //esp  - D1 <=> GPIO5
#define POT_PIN A0 //potenziometro
#define W_MAX 500

char* ssidName = "EOLO - FRITZ!Box PD"; //nome rete Wi-Fi
char* psw = "60352511842616029898"; //password rete Wi-Fi
String address = "http://37f130db2c40.ngrok.io";

Potentiometer* pot;
Led* ledAvail;
Led* ledNotAvail;
HTTPClient http;
StaticJsonDocument<200> doc; /*libreria arduinoJson*/
char json[500];


String value = {};
int currentWeight = 0;

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssidName, psw);
  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("[DEBUG] Connected: \n local IP: " + WiFi.localIP().toString());


  pot = new Potentiometer(POT_PIN);
  ledAvail = new Led(LED_AVAIL_PIN);
  ledNotAvail = new Led(LED_NOTAVAIL_PIN);
  ledNotAvail -> switchOff();
  ledAvail -> switchOn();
}

void sendData(String address, String value, String stringPath) {
  Serial.println("[DEBUG] Sending something...");
  http.begin(address + "/api/" + stringPath);
  http.addHeader("Content-Type", "application/json");
  String msg = "{ \"w\":" + value + "}";
  int retCode = http.POST(msg);

  if (retCode == 200) {
    Serial.println("Data sent succesfully (200)");
  } else {
    Serial.println("Error while sending data (" + (String)retCode + ")");
  }
  http.end();
}

int getWeight() {
  int val;
  val = pot -> readValue(1, 100);
  Serial.println("Val = " + (String)val);
  return val;


  // return (pot -> readValue(1, 100)); //usata altra versione per il debug
}

void loop() {

  if (WiFi.status() == WL_CONNECTED) { //controllo della connessione Wi-Fi

    http.begin(address + "/api/statoEdge"); //inizio della connessione html, che riceve in risposta il json con informazioni rilevanti per Arduino
    http.GET();
    String payload = http.getString();
    Serial.println("[DEBUG] Payload = " + payload);

    payload.toCharArray(json, 500);
    deserializeJson(doc, json);

    if (doc["reqEmpty"] == "true") { // se è stato richiesto lo svuotamento del cestino imposta a 0 il peso
      currentWeight = 0;
      Serial.println("[DEBUG] Bidone svuotato!");
    }

    if (doc["disp"] == "true") { // controlla se si è ancora nello stato available
      Serial.println("Dumpster available (0)");
      ledNotAvail -> switchOff();
      ledAvail -> switchOn();

      if (doc["reqWeight"] == "true") { //se è stato richiesto il peso, invia il messaggio con il peso

        currentWeight += getWeight();

        if (currentWeight >= W_MAX) {
          sendData(address, "u", "full");
        }
        Serial.println("[DEBUG] currentWeight = " + (String)currentWeight);
        sendData(address, (String)currentWeight, "weight");
      }


    } else {
      Serial.println("Dumpster not available (-1)");
      ledAvail -> switchOff();
      ledNotAvail -> switchOn();
    }



    http.end(); //chiudi connessione aperta inizialmente
  } else {
    Serial.println("Wifi Error (-2)");
  }
  delay(8000); //delay necessario per evitare di riempire il server ngrok di richieste
}
