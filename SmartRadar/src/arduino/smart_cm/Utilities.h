#ifndef __UTILITIES__
#define __UTILITIES__


#include "Scheduler.h"
#include "PirSleep.h"
#include "Task.h"
#include "BlinkTask.h"
#include <avr/sleep.h>
#include <avr/power.h>
#include "TimerOne.h"
#include "ButtonListener.h"
#include "ModeSetter.h"
#include "EngineServo.h"
#include "UltraS.h"

//VARIABILI
extern int distance;
extern int setAngle;
extern int currentAngle;
extern bool servoInPosition;
extern int currentMode;
extern int tempoUtente;
extern bool startScan;
extern bool scanned;
extern bool alarm;
extern bool detected;
extern bool tempAlarm;
extern Scheduler scheduler;
extern ModeSetter mS;
extern Task* pirSleep;
extern Task* scan;
extern Task* leds;
extern Task* buttonListener;
extern Task* readDistance;
extern Task* moveEngine;
extern unsigned int i;
extern int deltaAngle;
extern float coeff;
extern bool goingRight;
extern int scanCompleted;
extern unsigned int N;
extern int startPulseScan;
extern long int lastDetect;
extern int lastSegmentScanned;
extern int lastMode;
extern int scanTime;


#define COEFF_SOUND_SPEED 0.0171925
#define DNEAR 20
#define DFAR  40

#define MAX_TIME 10
#define MIN_TIME 2
//#define scanTime 5  //millisecondi per angolo - N*scanTime = 1800 millis


//circuito
#define PIR_PIN 9
#define POT_PIN  A5
#define TRIG_PIN 8
#define ECHO_PIN 7
#define LED_A 11
#define LED_D 10
#define ENGINE_PIN 6
#define TM1 A0
#define TM2 A1
#define TM3 A2

#endif
