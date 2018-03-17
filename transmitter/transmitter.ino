#include<SoftwareSerial.h>
#include<NewPing.h>
NewPing sonar(9,12);
SoftwareSerial btserial(2,3);
const int trig = 12;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  btserial.begin(9600);
  pinMode(13, OUTPUT);
  pinMode(trig, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(btserial.available())
  {
    byte c = btserial.read();
    /*digitalWrite(trig, LOW);
    delayMicroseconds(2);*/
    if(c)
    {
      /*digitalWrite(trig, HIGH);
      delayMicroseconds(10);
      digitalWrite(trig, LOW);*/
      sonar.ping();
      Serial.println("Ping...");
    }
  }
}
