#include<SoftwareSerial.h>
SoftwareSerial bls(2,3);

void setup() {
  // put your setup code here, to run once:
  pinMode(13, OUTPUT);
  Serial.begin(9600);
  bls.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available())
    {
      char c = Serial.read();
      bls.write(c);
      Serial.write(c);
    }
  if(bls.available())
    Serial.write(bls.read());
    
}
