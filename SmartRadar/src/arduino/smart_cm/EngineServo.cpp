#include "EngineServo.h"
#include "Utilities.h"
#include "ServoTimer2.h"


EngineServo::EngineServo(int enginePin) {
  this->enginePin = enginePin;
  engine = new ServoTimer2();
}

void EngineServo::init(int period) {
  Task::init(period);
  engine->attach(this->enginePin);
}


void EngineServo::tick() {
  //  Serial.println("SERVO TICK");



  if (currentMode != 2) {

    if (lastMode == 2) { //se vado da manual mode ad un'altra modalità allora resetto la posizione a quella precedente più vicina
      lastMode = 0;

      //    Serial.println((engine->read() - (MIN_PULSE_WIDTH)));
      //    Serial.println((( MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / (N)));
      i = ((engine->read() - (MIN_PULSE_WIDTH)) / (( MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / (N)));

      if (((engine->read() - (MIN_PULSE_WIDTH)) % (( MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / (N))) > (( MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / (2 * N))) {
        i++;
      }
      //MICHI
      //engine -> write(((engine->read() - (MIN_PULSE_WIDTH)) / (( MAX_PULSE_WIDTH - MIN_PULSE_WIDTH) / (N))));
    }
    //    Serial.print("i = ");
    //    Serial.println(i);

    if (startScan == 1) { //se il pir rileva qualcosa oppure se sono modalità auto
      startPulseScan = i;
      startScan = 0;
      if(i == 0 || i == N && currentMode == 1){
        scanCompleted++;
      }
      if ((i != 0 || i != N) && (currentMode == 1)) {
        moveEngine->init(tempoUtente * 500 / (N-1));
        readDistance->init(tempoUtente * 500 / (N-1));
      } else {
        moveEngine->init(tempoUtente * 1000 / (N-1));
        readDistance->init(tempoUtente * 1000 / (N-1));
      }
    }


    if (i == (N)) {
      goingRight = 0;
    }
    if (i == 0) {
      goingRight = 1;
    }
    if (goingRight == 1) {
      //      Serial.println("I'm here :)");
      i++;
    }
    if (goingRight == 0)
    {
      i--;
    }






    if (i == startPulseScan) { //se sono nello stesso punto di quando è cominciata la scansione aumento scanCompleted
      scanCompleted++;
    }
    if ((startPulseScan == 0 && i == N) || (startPulseScan == N && i == 0)) //se parto da 0 o da 180 allora la scansione finisce dopo 180 gradi, all'angolo opposto
      scanCompleted = 2;

    if (scanCompleted == 2) {     //se è la seconda volta che ci passo, ho finito la scansione (prima parte di scans, seconda parte scans)
      scanCompleted = 0;
      /* if (goingRight == 1) {
         i--;
        }
        if (goingRight == 0)
        {
         i++;
        }*/

      if (currentMode == 1) {
        moveEngine->setActive(false); //se modalità 1 disattivo il movimento del servo e aspetto che il pir lo riattivi
        readDistance->setActive(false);
        pirSleep->setActive(true);    //se modalità 1
        detected = 0;
        Serial.println("b0");
      } else if (currentMode == 3) { //se sono in auto invece considero che il pir sia sempre acceso
        startScan = 1;                //anche quando finisco una scansione
        if (tempAlarm == 1) {
          tempAlarm = 0;
        } else if (tempAlarm == 0) {
          alarm = 0;
          Serial.println("a0");
        }
      }
    }


    //se la scansione non è stata completata allora abilita la lettura della distanza e sposta in servo
    engine -> write(MIN_PULSE_WIDTH + (deltaAngle * coeff * i));
    lastSegmentScanned = engine->read();
    currentAngle = (deltaAngle * i);
    setAngle = (engine->read() - MIN_PULSE_WIDTH) / coeff; //aggiorno setAngle per la modalità manual

  }

  if (currentMode == 2) {
    engine -> write(MIN_PULSE_WIDTH + (setAngle * coeff));
    //    Serial.println("Pulse = " + (String)engine->read());
    moveEngine->setActive(false);
    lastMode = 2;
  }

  readDistance->setActive(true);
}
