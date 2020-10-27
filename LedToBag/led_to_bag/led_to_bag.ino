#include <avr/io.h>
#include <avr/wdt.h>
#include "Function.h"
#define Reset_AVR() wdt_enable(WDTO_2S); while(1) {}
#define LB 9
#define LR 13
#define L1 5
#define L2 6
#define L3 10
#define POT A5
#define TD 3
#define TS 2
#define DELAYCOUNTDOWN 100
#define BOUNCING_TIME 200

int Lpos = 0;
int score = 0;
unsigned long deltaTime = 0;
unsigned long gameTime = 0;
bool Tflag = 0;
bool gameStartFlag = 0;
int  prevTime = 0;
int brightness = 0;
int fadeAmount = -5;

void setup() {
  pinMode(LB, OUTPUT);
  pinMode(LR, OUTPUT);
  pinMode(L1, OUTPUT);
  pinMode(L2, OUTPUT);
  pinMode(L3, OUTPUT);
  pinMode(TS, INPUT);
  pinMode(TD, INPUT);
  Serial.begin(9600);

  Serial.println("\t \t \t \t \t Welcome to Led to Bag!");
  Serial.println("\t \t \t \t \t Press Key TS to Start!");
  randomSeed(analogRead(A2));
  attachInterrupt(digitalPinToInterrupt(TS), gameStarted, FALLING);

  while (!gameStartFlag) {
    fade(1);
    fade(2);
    fade(3);
    fade(2);
  }

  digitalWrite(L1, LOW);
  digitalWrite(L2, LOW);
  digitalWrite(L3, LOW);
  detachInterrupt(digitalPinToInterrupt(TS));
  attachInterrupt(digitalPinToInterrupt(TD), decPos, RISING);
  int difficult = analogRead(POT);
  difficult = map(difficult, 0, 1024, 1, 8);
  deltaTime = 500 * (9 - difficult);
  Serial.println("\t \t \t Difficult set to: " + String(difficult) + " !");
  countDown();
}


void gameStarted() {
  gameStartFlag = 1;
  brightness = 0;
}


void fade(int i) {

  int led = i;

  if (i == 1)
    led = L1;
  if (i == 2)
    led = L2;
  if (i == 3)
    led = L3;

  fadeAmount = -fadeAmount;
  brightness = 0;
  while (gameStartFlag == 0) {
    brightness += fadeAmount;
    analogWrite(led, brightness);
    if (brightness >= 255)
      fadeAmount = -fadeAmount;
    if (brightness <= 0)
      break;
    delay(20);
  }
}


// TODO il loop deve contenere il turno, da quando si preme il tasto, viene randomizzato il led
void loop() {
  // Controllo se devo randomizzare un nuovo led
  if (Tflag == 0) {
    gameTime = millis();
    Lpos = random(1, 4);
    digitalWrite(LB, LOW);
    if (Lpos == 1) {
      digitalWrite(L1, HIGH);
    }
    if (Lpos == 2) {
      digitalWrite(L2, HIGH);
    }
    if (Lpos == 3) {
      digitalWrite(L3, HIGH);
    }
    Tflag = 1;
  }


  if (Lpos == 0) {

    analogWrite(LB, brightness);

    brightness += fadeAmount;

    if (brightness >= 255 || brightness <= 0)
      fadeAmount = -fadeAmount;
      delay(2);
  }


  if (Lpos < 0) {

    digitalWrite(LR, HIGH);
    digitalWrite(LB, LOW);
    digitalWrite(L1, LOW);
    digitalWrite(L2, LOW);
    digitalWrite(L3, LOW);
    gameOver();
  }
  checkTime();
}


void countDown() {
  int a;
  //inizia il conto alla rovescia da 3
  for (a = 3; a >= 1;  a--) {
    Serial.print("\t \t \t  " + (String)(a));
    delay(DELAYCOUNTDOWN);
    Serial.print(".");
    delay(DELAYCOUNTDOWN);
    Serial.print(".");
    delay(DELAYCOUNTDOWN);
    Serial.println(".");
  }
  Serial.println("\t \t \t GO!!! \n");
}

void checkTime() {
  if (millis() - gameTime > deltaTime)
    checkPos();
  if (Lpos < 0) {
    digitalWrite(LB, LOW);
    gameOver();
  }

}

void checkPos() {   //implementare win/lose
  //LOSE

  if (Lpos != 0)
    gameOver();

  //WIN
  if (Lpos == 0) {
    score++;
    Serial.println("\t Another item in the bag! Current Score: " + (String)(score));
    deltaTime = deltaTime * 7 / 8;
    digitalWrite(L1, LOW);
    digitalWrite(L2, LOW);
    digitalWrite(L2, LOW);
    digitalWrite(LB, LOW);
    brightness = 0;
    fadeAmount = abs(fadeAmount);
  }
  delay(300);
  Tflag = 0;

  loop(); //???????????????????????????????????????????????????????
}


void decPos () {
  if ((micros() * 1000) - prevTime > BOUNCING_TIME) {
    Lpos--;
  }

  if (Lpos == 2) {
    digitalWrite(L2, HIGH);
    digitalWrite(L3, LOW);
  }
  if (Lpos == 1) {
    digitalWrite(L1, HIGH);
    digitalWrite(L2, LOW);
  }

  if (Lpos == 0)
    digitalWrite(L1, LOW);

  prevTime = (micros() * 1000);
}

void gameOver() {

  gameStartFlag = 0;
  noInterrupts();
  digitalWrite(L1, LOW);
  digitalWrite(L2, LOW);
  digitalWrite(L3, LOW);
  digitalWrite(LB, LOW);
  digitalWrite(LR, HIGH);
  Serial.println("\n \n \n \t \t \t \t \t       Game Over!");
  Serial.println("\n \t \t \t \t \t    Final Score: " + (String)(score)+ "\n \n \n \n \n");
  detachInterrupt(digitalPinToInterrupt(TD));
  detachInterrupt(digitalPinToInterrupt(TS));
  interrupts();
  Reset_AVR();
}

