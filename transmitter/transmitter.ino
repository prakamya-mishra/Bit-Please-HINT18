#include<SoftwareSerial.h>
const int rx = 2;
const int tx = 3;
const int trig = 10;
SoftwareSerial btserial(rx,tx);

void setup() {
  // put your setup code here, to run once:
  pinMode(trig, OUTPUT);
  Serial.begin(9600);
  btserial.begin(9600);
  digitalWrite(trig, LOW);
}

void loop() {
  // put your main code here, to run repeatedly:
  char control;
  while((control = Serial.read()) == '0');
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
}
