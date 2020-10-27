#include "Potentiometer.h"
#include "Arduino.h"

Potentiometer::Potentiometer(int pin){
  this->pin = pin;
}

int Potentiometer::readValue(int qmin, int qmax){
  return map(analogRead(pin), 0, 1023, qmin, qmax);  
}
