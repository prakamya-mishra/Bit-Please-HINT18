#include<SoftwareSerial.h>
SoftwareSerial btserial(2,3);


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  btserial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(btserial.available())
  {
    byte c = btserial.read();
    Serial.println(c);
  }
}
