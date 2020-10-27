#ifndef PIRSLEEP
#define PIRSLEEP

#include "Task.h"

class PirSleep: public Task {


  public:
    PirSleep(int pirPin);
    void init(int period);
    void tick();
  private:
    int period;
    int pirPin;
};
#endif
