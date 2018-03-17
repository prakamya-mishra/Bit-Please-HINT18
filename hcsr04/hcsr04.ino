void setup() {
  // put your setup code here, to run once:
  pinMode(12,OUTPUT);
  pinMode(9, INPUT);
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(12, LOW);
  delayMicroseconds(2);
  digitalWrite(12, HIGH);
  delayMicroseconds(10);
  digitalWrite(12, LOW);
  long duration = pulseIn(9, HIGH);
  Serial.println(duration);
}
