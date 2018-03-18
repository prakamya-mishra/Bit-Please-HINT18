#include<SoftwareSerial.h>
#include<NewPing.h>

NewPing left(12,9);
NewPing right(11,8);
const int rx = 2;
const int tx = 3;
long durationL, durationR;
const int echoL = 12;
const int echoR = 11;
const int trigL = 9;
const int trigR = 8;
int motorL = 0;
int motorR = 0;
#define SPEED 343

SoftwareSerial btserial(rx,tx);

void setup() {
  
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  btserial.begin(9600);
}

void loop() {
    
    //delay(1000);
  
 // btserial.write(2);
  //Serial.println("1");
  btserial.write(2);
  long distL = left.ping();
  long distR = right.ping();
  //Serial.println("L "+(String)distL+ " R "+(String)distR);
  //Serial.println(distR);
  delay(100);
  motorL = map(distL, 0, 15000, 0, 255);
  motorR = map(distR, 0, 15000, 0, 255);
  Serial.println("L: "+(String)motorL+" R: "+(String)motorR);
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
