#ifndef __ULTRAS__
#define __ULTRAS__

#include "Task.h"
#include "Utilities.h"

class UltraS: public Task {

  public:
    UltraS(int trigPin, int echoPin);
    void init(int period);
    void tick();
  private:
    int period;
    int trigPin;
    int echoPin;
    int prevdistance;
};

#endif
