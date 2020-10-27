#include "BlinkTask.h"
#include "Utilities.h"



Blink::Blink(int pinA, int pinD) {
  this->pinA = pinA;
  this->pinD = pinD;

}

void Blink::init(int period) {
  Task::init(period);
  ledA = new Led(pinA);
  ledD = new Led(pinD);
  stateA = OFFa;
  stateD = OFFd;

}

void Blink::tick() {
  
  if (alarm == 1) {
    switch (stateA) {

      case OFFa:
        ledA->switchOn();
        stateA = ONa;
        break;

      case ONa:
        ledA->switchOff();
        stateA = OFFa;
        break;
    }
  } else {
    ledA->switchOff();
    stateA = OFFa;
  }

  if (detected == 1) {
    switch (stateD) {
      case OFFd:
        ledD->switchOn();
        stateD = ONd;
        break;

      case ONd:
        ledD->switchOff();
        stateD = OFFd;
        break;
    }

  } else {
    ledD->switchOff();
    stateD = OFFd;
  }
}
