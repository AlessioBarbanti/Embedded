#include "Utilities.h"

//Authors: Alessio Barbanti, Michele Torroni, Andrea Tassani

unsigned int N = 5 - 1;
int distance = 0;
int tempAngle = 0;
int setAngle ;
int currentAngle;
int deltaAngle;
bool servoInPosition = 0;
bool scanned;
bool alarm = 0;
bool detected = 0;
bool tempAlarm = 0;
int currentMode = 2;
int tempoUtente = (MAX_TIME + MIN_TIME) / 2;
bool startScan;
unsigned int i = 0;
float coeff = (MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / 180;
bool goingRight = 1;
int scanCompleted = 0;
int startPulseScan;
long int lastDetect = 0;
int lastSegmentScanned;
int lastMode = 0;
int scanTime = 5;


Scheduler scheduler;
ModeSetter mS;


Task* leds;
Task* pirSleep;
Task* moveEngine;
Task* readDistance;
Task* buttonListener;


void setup() {

  Serial.begin(115200);
  Serial.flush();
  //  Serial.print("scanTime * deltaAngle = ");
  //  Serial.println(scanTime * deltaAngle);

  delay(2000);
  /*
    Serial.println("Calibrating sensor... ");
    for (int i = 0; i < 5; i++) {
      Serial.println(".");
      delay(1000);
    }*/

  if (Serial.peek() == 'k') {
    Serial.read();
    N = Serial.parseInt();
    //Serial.println("N set to: " + (String)N);
  }

  deltaAngle = 180 / N;
  scheduler.init(50);
  readDistance = new UltraS(TRIG_PIN, ECHO_PIN);
  buttonListener = new ButtonListener(TM1, TM2, TM3);
  leds = new Blink(LED_A, LED_D);
  pirSleep = new PirSleep(PIR_PIN);
  moveEngine = new EngineServo(ENGINE_PIN);

  scheduler.addTask(buttonListener);
  scheduler.addTask(leds);
  scheduler.addTask(readDistance);
  scheduler.addTask(moveEngine);
  scheduler.addTask(pirSleep);

  //inizializzo tutti i task
  moveEngine->init(tempoUtente * 1000 / N);
  readDistance->init(tempoUtente * 1000 / N);
  pirSleep->init(40);
  leds->init(30);
  buttonListener-> init(50);





  moveEngine->setActive(true);
  leds->setActive(true);
  buttonListener->setActive(true);
  pirSleep->setActive(false);
  readDistance->setActive(true);

  mS.setMode(currentMode);


}

void loop() {

  //  Serial.println("******************************************");
  //  Serial.println("CurrentMode: " + (String)currentMode);
  //  Serial.println("moveEngine: " + (String)moveEngine->isActive());
  //  Serial.println("leds: " + (String)leds->isActive());
  //  Serial.println("button: " + (String)buttonListener->isActive());
  //  Serial.println("pirSleep: " + (String)pirSleep->isActive());
  //  Serial.println("readDistance: " + (String)readDistance->isActive());
  //  Serial.println("******************************************");

  scheduler.schedule();
}

void serialEvent() { //ogni volta che finisce il loop viene chiamata questa funzione, per vedere se arrivano nuovi dati.

  char serial;
  while (Serial.available()) {
    serial = (char)Serial.read(); //Leggo il primo valore della seriale per vedere che informazione arriva

    if (serial != '\n') {
      switch (serial) {

        case 't':
          if (Serial.peek() == 'p') {
            tempoUtente = map(analogRead(POT_PIN), 0, 1024, MIN_TIME, MAX_TIME);
            Serial.read();
          }
          else {
            int tTemp = Serial.parseInt();
            if (tTemp <= MAX_TIME && tTemp >= MIN_TIME) // 2- 10
              tempoUtente = tTemp;

          }

          moveEngine->init((tempoUtente * 1000 / N));
          break;


        case 'd':
          setAngle = Serial.parseInt();
          break;

        case 's':                                                  //SINGLE

          //Serial.println("Mode set to: SINGLE (1)");
          mS.setMode(1);
          break;

        case 'm':                                                    //MANUAL
          //Serial.println("Mode set to: MANUAL (2)");
          mS.setMode(2);
          break;

        case 'a':                                                     //AUTO
          mS.setMode(3);
          //Serial.println("Mode set to: AUTO (3)");
          break;



        default:
          //Serial.print("Command Not Found! : ");
          //Serial.println(serial);
          break;
      }
    }
  }
}
