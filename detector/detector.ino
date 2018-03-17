#include<SoftwareSerial.h>

const int rx = 2;
const int tx = 3;
long durationL, durationR;
const int echoL = 12;
const int echoR = 10;
const int trigL = 9;
const int trigR = 8;
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
    
    //delay(1000);
  
 // btserial.write(2);
  //Serial.println("1");
  btserial.write(2);
  digitalWrite(trigR, LOW);
  digitalWrite(trigL, LOW);
  delayMicroseconds(2);
  digitalWrite(trigR, HIGH);
  digitalWrite(trigL, HIGH);
  delayMicroseconds(10);
  durationL = pulseIn(12, HIGH);
  durationR = pulseIn(10, HIGH);
  long distL = SPEED * durationL;
  long distR = SPEED * durationR;
  String dL = (String)distL;
  String dR = (String)distR;
  Serial.println("L :" + dL + " R: "+ dR);
  delay(2000);
  
  //btserial.write("0");
  //delay(2000);
  /*if(Serial.available())
    {
      char c = Serial.read();
      btserial.write(c);
    }
  if(btserial.available())
  {
    char c = Serial.read();
    Serial.write(c);
  }*/
    
}
