#ifndef __SERVOMOTOR__
#define __SERVOMOTOR__

#include "Arduino.h"
#include "ServoTimer2.h"

class ServoMotor {

public:
  ServoMotor(int pin);

  void on();
  void setPosition(int angle);
  void off();
    
private:
  int pin; 
  ServoTimer2 motor; 
};

#endif
