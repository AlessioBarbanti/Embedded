#ifndef __POTENTIOMETER__
#define __POTENTIOMETER__

class Potentiometer{ 
public:
  Potentiometer(int pin);  
  int readValue(int qmin, int qmax);

private:
  int pin;
};

#endif
