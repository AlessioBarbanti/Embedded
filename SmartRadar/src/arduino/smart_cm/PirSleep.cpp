#include "PirSleep.h"
#include "Utilities.h"

PirSleep::PirSleep(int pirPin) {
  this->pirPin = pirPin;


}

void PirSleep::init(int period) {
  Task::init(period);
  this->period = period;
  pinMode(this->pirPin, INPUT);
}


void PirSleep::tick() {

//  Serial.println("pir tick");
  moveEngine->setActive(false);
  readDistance->setActive(false);
  leds->setActive(false);

  detected = 0;
  Serial.println("b0");

  startScan = digitalRead(pirPin);

  if (startScan) { //se il pir trova qualcosa allora il suo prossimo tick sarà un secondo dopo la fine della scansione
    pirSleep->init((scanTime * 360) + 1000);
//    Serial.println("Next scan in: " + (String)(scanTime * 2 * 180 + 1000));
    pirSleep->setActive(false);
    moveEngine->setActive(true);
    leds->setActive(true);
  }
  else {        // se invece non trova nulla controlla ogni 40 ms se c'è un movimento
    pirSleep->init(40);
  }
}
