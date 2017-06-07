#include <SoftwareSerial.h>
#include <VirtualWire.h>
#include <QueueArray.h>

SoftwareSerial S(3, 4);   //bluetooth module Tx:Digital 2 Rx:Digital 3
unsigned char counter;
QueueArray<unsigned long> queue;
unsigned long temp[21];
unsigned long prevTime;
bool data_effect = true;
unsigned int heart_rate;//the measurement result of heart rate

const int max_heartpluse_duty = 2000;//you can change it follow your system's request.

void setup() {
  Serial.begin(9600);
  S.begin(9600);
  Serial.println("Please ready your chest belt.");
  delay(500);
  arrayInit();
  Serial.println("Heart rate test begin.");
  attachInterrupt(0, interrupt, RISING);//set interrupt 0,digital port 2
}

void loop() {

}

void sum(unsigned long currentTime) {
  heart_rate = 1200000 / (currentTime - queue.dequeue()); //60*20*1000/20_total_time
  Serial.println(heart_rate);
  S.println(String(heart_rate));
}

void interrupt() {
  unsigned long currentTime = millis();
  queue.enqueue(currentTime);
  if (queue.count() == 1) prevTime = currentTime;
  if (currentTime - prevTime > max_heartpluse_duty) {
    Serial.println("Error!");
    arrayInit();
  }
  prevTime = currentTime;
  if (queue.count() == 20) sum(currentTime);
}

void arrayInit() {
  while (!queue.isEmpty()) queue.dequeue();
}

