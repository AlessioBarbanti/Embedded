#include "MsgServiceBT.h"
#include "SoftwareSerial.h"
#include "Led.h"
#include "ServoMotor.h"
#include <TimerOne.h>



#define LED_A_PIN 8
#define LED_B_PIN 9
#define LED_C_PIN 10
#define SERVO_PIN 6
#define TXD 3 //BT
#define RXD 2 //BT


static unsigned long int delta_time = 60000; //se dichiarate con define errore nell'if riga ~104
static unsigned long int tick_timer = 30000;

Led* ledA;
Led* ledB;
Led* ledC;
ServoMotor* motor;
MsgServiceBT msgService(TXD, RXD);
unsigned long initialTime = 0;
bool state = 0;
int extendCount = 0;


void machineStep(String content) {
  //Stato "deposito in corso"
  /*
    se si è ricevuta una lettera allora si passa allo stato "attivo", viene
    aperto il portellone (Servo) e inizia il conto alla rovescia per la chiusura
  */

  if (content == "A" || content == "B" || content == "C") {
    state = 1;
    initialTime = millis();
    motor->setPosition(180);
    Serial.println("initial time = " + (String)initialTime);

  }

  /*
    Accendo il led corrispondente
  */
  
  if (content == "A") {
    Serial.println("[DEBUG] LedA ON");
    ledA->switchOn();
  } else if (content == "B") {
    Serial.println("[DEBUG] LedB ON");
    ledB->switchOn();
  } else if (content == "C") {
    Serial.println("[DEBUG] LedC ON");
    ledC->switchOn();
  } else if (content == "E") {
    extendCount++;
    Serial.println("millis() - initialTime = " + (String)(millis() - initialTime));
    Serial.print("DELTA_TIME + TICK_TIMER * extendCount = ");
    Serial.println((delta_time + (tick_timer * extendCount)) / 1000);
    Serial.println("extendCount: " + (String)(extendCount));

  }


  //Stato "chiuso" -- sequenza di chiusura
  if (content == "d") {
    ledA->switchOff();
    ledB->switchOff();
    ledC->switchOff();
    motor->setPosition(0);
    state = 0;
    extendCount = 0;

  }
}


void setup() {
  Timer1.initialize(1000000); //1000000 usec = 1 sec
  msgService.init();
  Serial.begin(115200);

  Serial.println("READY!");

  ledA = new Led(LED_A_PIN);
  ledB = new Led(LED_B_PIN);
  ledC = new Led(LED_C_PIN);
  motor = new ServoMotor(SERVO_PIN);
  motor->on();
  state = 0;
}

void loop() {
  if (msgService.isMsgAvailable()) {
    Msg* msg = msgService.receiveMsg();
    const String content = msg->getContent();
    Serial.println("[DEBUG] content1 = " + content);
    machineStep(content);
  } else {
    unsigned long int value = delta_time + tick_timer * extendCount;
    if (  millis() - initialTime  > value && state == 1) {
      Serial.println("millis() - initialTime = " + (String)(millis() - initialTime));
      Serial.println("DELTA_TIME + TICK_TIMER * extendCount = " + (String)(delta_time + (tick_timer * extendCount)));
      machineStep("d");
    }
  }
}
