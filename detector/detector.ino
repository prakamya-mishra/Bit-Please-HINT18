#include<SoftwareSerial.h>
#include<NewPing.h>

NewPing left(12,9,200);
NewPing right(7,8,200);
const int rx = 2;
const int tx = 3;
long durationL, durationR;
int motorL = 0;
int motorR = 0;
const int motorL1 = 11;
const int motorL2 = 10;
const int motorR1 = 1;
const int motorR2 = 0;
const int ENR = 6;
const int ENL = 5;
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
    long distL = left.ping(); //Time to reach the sensor
    long distR = right.ping();
    //Serial.println("L "+(String)distL+ " R "+(String)distR);
    //Serial.println(distR);
    
    /*motorL = map(distL, 0, 2000, 0, 255);
    motorR = map(distR, 0, 2000, 0, 255);
    analogWrite(ENR, motorR);
    analogWrite(ENL, motorL);
    digitalWrite(motorL1, HIGH);
    digitalWrite(motorL2, LOW);
    digitalWrite(motorR1, LOW);
    digitalWrite(motorR2, HIGH);*/
    //Serial.println("L: "+(String)motorL+" R: "+(String)motorR);
/********************************************************************************
 * Digital Motor Control
 ********************************************************************************/

    while(distL > distR)
    {
      digitalWrite(motorL1, HIGH);
      digitalWrite(motorL2, LOW);
    }
    while(distR > distL)
    {
      digitalWrite(motorR1, HIGH);
      digitalWrite(motorR2, LOW);
    }
    digitalWrite(motorL1, HIGH);
    digitalWrite(motorL2, LOW);
    digitalWrite(motorR1, LOW);
    digitalWrite(motorR2, HIGH);
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
    delay(100);
    
}
