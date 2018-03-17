#include<SoftwareSerial.h>
const int rx = 9;
const int tx = 10;
SoftwareSerial com(rx,tx);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  com.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available())
  {
    char c = Serial.read();
    com.write(c);
    Serial.write(c);
  }
  if(com.available())
  {
    Serial.write(com.read());
  }
}
