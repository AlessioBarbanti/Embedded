#ifndef __ButtonListener__
#define __ButtonListener__

#include "Utilities.h"
#include "Task.h"

class ButtonListener: public Task {
  
    int tm1;    
    int tm2;    
    int tm3;
  public:
    ButtonListener(int tm1, int tm2, int tm3);
    void init(int period);
    void tick();
};

#endif
