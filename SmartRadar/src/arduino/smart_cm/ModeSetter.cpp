#include "ModeSetter.h"
#include "Utilities.h"

void ModeSetter::setMode(int mode) {
  if (mode != currentMode) {
    digitalWrite(LED_A, LOW);
    digitalWrite(LED_D, LOW);
    detected = 0;
    alarm = 0;
    Serial.println("a0");
    Serial.println("b0");
    switch (mode) {

      case 1:                         //Single

        pirSleep->setActive(true);
        leds->setActive(true);
//        moveEngine->setActive(false);
//        readDistance->setActive(false);
        scanCompleted = 0;
        currentMode = 1;
        break;

      case 2:

        pirSleep->setActive(false);
        leds->setActive(false);
        moveEngine->setActive(true);
        readDistance->setActive(false);
        currentMode = 2;
        break;

      case 3:


        pirSleep->setActive(false);
        leds->setActive(true);
        moveEngine->setActive(true);
        readDistance->setActive(true);
        currentMode = 3;
        break;
    }
  }

}
