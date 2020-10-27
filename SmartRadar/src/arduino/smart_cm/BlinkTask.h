#ifndef __BLINKTASK__
#define __BLINKTASK__

#include "Task.h"
#include "Led.h"

class Blink: public Task {

    int pinA;
    int pinD;
    Led* ledA;
    Led* ledD;
    enum { ONa, OFFa} stateA;
    
    enum { ONd, OFFd} stateD;

  public:

    Blink(int pinA, int pinD);
    void init(int period);
    void tick();
};

#endif
