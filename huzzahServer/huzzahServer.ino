
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
const char* ssid = "AndroidAP";
const char* password = "coehv1902ha,vj";

ESP8266WebServer server(80); 

void handleRoot() {
 String s = "Distance: 23"; 
 server.send(200, "text/html", s); 
 
void setup(void){
  Serial.begin(9600);
  
  WiFi.begin(ssid, password);     
  Serial.println("");
  while (WiFi.status() != WL_CONNECTED);

  Serial.print("IP address: "+WiFi.localIP());
  server.on("/", handleRoot);      

  server.begin();                  
}

void loop(void){
  server.handleClient();          
}
