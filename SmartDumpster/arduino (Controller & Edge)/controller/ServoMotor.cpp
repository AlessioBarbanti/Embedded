#include "ServoMotor.h"
#include "Arduino.h"

ServoMotor::ServoMotor(int pin){
  this ->pin = pin;
}

void ServoMotor::on(){
  motor.attach(pin);    
}

void ServoMotor::setPosition(int angle){
  float coeff = (2344.0-544.0)/180;
  motor.write((int)(750 + angle*coeff));   
  Serial.println("[DEBUG] motor.write = " + (String)(750 + angle*coeff));

}

void ServoMotor::off(){
  motor.detach();    
}
