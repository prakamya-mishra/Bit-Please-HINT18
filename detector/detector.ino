#include<SoftwareSerial.h>

const int rx = 2;
const int tx = 3;
long durationL, durationR;
const int echoL = 12;
const int echoR = 10;
#define SPEED 343

SoftwareSerial btserial(rx,tx);

id setup() {
  
  pinMode(echoL, INPUT);
  pinMode(echoR, INPUT);
  Serial.begin(9600);
  btserial.begin(9600);
}

void loop() {
    
  btserial.println("1");
  durationL = pulseIn(12, HIGH);
  durationR = pulseIn(10, HIGH);
  btserial.println("0");
  long distL = SPEED * durationL;
  long distR = SPEED * durationR;
  Serial.println("L :" + distL + " R: "+ distR);
}
