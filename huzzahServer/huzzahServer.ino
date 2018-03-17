
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include<SoftwareSerial.h>
#include<NewPing.h>

NewPing left(14,12); //Trigger and echo
NewPing right(13,15);
const int rx = 2;
const int tx = 3;
long durationL, durationR;
const int echoL = 12;
const int echoR = 11;
const int trigL = 9;
const int trigR = 8;
#define SPEED 343
const char* ssid = "AndroidAP";
const char* password = "coehv1902ha,vj";

SoftwareSerial btserial(2,16); //Tx and Rx (Tx of bluetooth has to be connected to Rx of the MCU and vice-versa)
ESP8266WebServer server(80); 

void handleRoot() {
 String s = "DurationL: "+(String)durationL+" DurationR: "+(String)durationR; 
 server.send(200, "text/html", s); 

}
 
void setup(){
  Serial.begin(9600);
  
  WiFi.begin(ssid, password);     
  Serial.println("");
  while (WiFi.status() != WL_CONNECTED);

  Serial.print("IP address: "+WiFi.localIP());
  server.on("/", handleRoot);      

  server.begin();                  
}

void loop(){
  server.handleClient();  
  btserial.write(2);
  durationL = left.ping();
  durationR = right.ping();
  Serial.println("L "+(String)durationL+ " R "+(String)durationR);
  //Serial.println(distR);
  delay(100);        
}
