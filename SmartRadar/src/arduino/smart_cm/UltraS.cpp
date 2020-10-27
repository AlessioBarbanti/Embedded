#include "UltraS.h"
#include "Utilities.h"


UltraS::UltraS(int trigPin, int echoPin) {

  this->echoPin = echoPin;
  this->trigPin = trigPin;
}

void UltraS::init(int period) {
  Task::init(period);
  this->prevdistance = 0;
  this->period = period;
  pinMode(TRIG_PIN, OUTPUT);
  pinMode(ECHO_PIN, INPUT);
}

void UltraS::tick() {
//  Serial.println("tick ultra");

  digitalWrite(TRIG_PIN, LOW);
  delayMicroseconds(3);
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(5);
  digitalWrite(TRIG_PIN, LOW);
  long tUS = pulseInLong(ECHO_PIN, HIGH, (this->period) * 9000 / 10);
  distance = tUS * COEFF_SOUND_SPEED;
  readDistance->setActive(false);


  if (distance != prevdistance) {
    prevdistance = distance;
    Serial.println("L" + (String)distance);
    Serial.println("g" + (String)currentAngle);
  }

  if (currentMode == 1) {
    if ((distance < DFAR) && distance != 0) {
      detected = 1;
      Serial.println("b1");
    }
    else if (detected == 1) {
      detected = 0;
      Serial.println("b0");
      readDistance->setActive(false);
    }
  }


  if (currentMode == 3) {

    if (distance >= DNEAR && distance <= DFAR) {
      alarm = 1;
      tempAlarm = 1;
      moveEngine->setActive(true);
      readDistance->setActive(false);
      Serial.println("a1");
    } else if (distance <= DNEAR && distance != 0) {
      alarm = 1;
      tempAlarm = 1;
      moveEngine->setActive(false);
      readDistance->setActive(true);
      Serial.println("a2");
    } else if (distance >= DFAR || distance == 0) {
      //readDistance->setActive(true);
      moveEngine->setActive(true);
    } else if (alarm == 1) {
      moveEngine->setActive(true);
    }
  }

  if (currentMode == 2) {
    moveEngine->setActive(true);
  }
}
