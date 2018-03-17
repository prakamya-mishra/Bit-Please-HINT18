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
  btserial.begin(38400);
}

void loop() {
    
 /* btserial.println("1");
  durationL = pulseIn(12, HIGH);
  durationR = pulseIn(10, HIGH);
  btserial.println("0");
  long distL = SPEED * durationL;
  long distR = SPEED * durationR;
  String dL = (String)distL;
  String dR = (String)distR;
  
  Serial.println("L :" + dL + " R: "+ dR);
  delay(2000);*/
  /*if (Serial.available())
    btserial.write(Serial.read());
  // Keep reading from HC-05 and send to Arduino Serial Monitor
  if(btserial.available())
    Serial.write(btserial.read());
*/
  if(btserial.available())
    {
      if(btserial.read() == '1')
        digitalWrite(13, HIGH);
    }
  // Keep reading from Arduino Serial Monitor and send to HC-05
  
}
