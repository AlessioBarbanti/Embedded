#ifndef __SERVO__
#define __SERVO__

#include "Task.h"
#include "ServoTimer2.h"
#include "Utilities.h"

class EngineServo: public Task {

  public:
    EngineServo(int enginePin);

    void init(int period);
    void tick();
    ServoTimer2* engine;
  private:
    int enginePin;
};
#endif
