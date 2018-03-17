#include<SoftwareSerial.h>

const int rx = 2;
const int tx = 3;
long durationL, durationR;
const int echoL = 12;
const int echoR = 10;
#define SPEED 343

SoftwareSerial btserial(rx,tx);

void setup() {
  
  pinMode(echoL, INPUT);
  pinMode(echoR, INPUT);
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  btserial.begin(9600);
}

void loop() {

  btserial.write("1");
  /*durationL = pulseIn(12, HIGH);
  durationR = pulseIn(10, HIGH);
  btserial.write("0");
  long distL = SPEED * durationL;
  long distR = SPEED * durationR;
  String dL = (String)distL;
  String dR = (String)distR;
  
  Serial.println("L :" + dL + " R: "+ dR);*/
  delay(2000);
  btserial.write("0");
  delay(2000);
  
}
