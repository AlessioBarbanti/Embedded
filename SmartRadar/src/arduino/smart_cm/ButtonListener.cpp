#include "Utilities.h"
#include "ButtonListener.h"

ButtonListener::ButtonListener(int tm1, int tm2, int tm3) {
  this->tm1 = tm1;
  this->tm2 = tm2;
  this->tm3 = tm3;

  pinMode(tm1, INPUT);
  pinMode(tm2, INPUT);
  pinMode(tm3, INPUT);
}

void ButtonListener::init(int period) {
  Task::init(period);
}


void ButtonListener::tick() {
  if (digitalRead(tm1) == HIGH && currentMode != 1) {
    Serial.println("s");
    //mS.setMode(1);
  }
  if (digitalRead(tm2) == HIGH && currentMode != 2) {
    Serial.println("m");
    //mS.setMode(2);
  }
  if (digitalRead(tm3) == HIGH && currentMode != 3) {
    Serial.println("a");
    //mS.setMode(3);
  }
}
