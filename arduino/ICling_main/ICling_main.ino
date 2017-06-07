#include <SoftwareSerial.h>
#include "Wire.h"
#include "I2Cdev.h"
#include "MPU9250.h"
#include "QueueArray.h"

#define SEND_TIME 200
#define MAX_SAME_SPEED 8
#define MAX_ANGLE 55

unsigned long prev_time;
unsigned long next_time;

double bike_speed;
double bike_distance;
int handle_angle;
int heart_rate;

// 방향제어
MPU9250 mpu;
int16_t AcX, AcY, AcZ;
int16_t GyX, GyY, GyZ;
int16_t MgX, MgY, MgZ;
int sValue;

// 속도제어
char bi_buf[100];
int bi_i = 0;
float prevSpeed = 0.0;
int sameSpeedFlag = 0;

// 심박제어
char hr_buf[100];
int hr_i = 0;

// 데이터제어
SoftwareSerial MBT(12, 13); // 블루투스
boolean startFlag = false;

void setup() {

  // ## inital sensor ## //
  Serial.begin(115200);
  Serial2.begin(9600);
  Serial3.begin(9600);
  MBT.begin(115200);
  Wire.begin();
  mpu.initialize();

  // ## inital data ## //
  initData();
}

void loop() {
  if (MBT.available()) {
    String data = MBT.readString();
    char command = data.charAt(0);
    if (command == 'W') {
      Serial2.println(data);
      MBT.println("WSuccess");
      Serial.println("WSucccess");
    } else if (command == 'R') {
      Serial2.println("R");
    } else if (command == 'S') {
      initData();
      Serial2.println("S");
      startFlag = true;
    } else if (command == 'E') {
      Serial2.println("E");
      startFlag = false;
    } else if (command == 'B') {
      Serial.println(data);
      Serial3.println(data);
    } else {
      Serial.println(data);
    }
  }

  if (Serial.available()) {
    String data = Serial.readString();
    char command = data.charAt(0);
    if (command == 'W') {
      Serial2.println(data);
      MBT.println("WSuccess");
      Serial.println("WSucccess");
    } else if (command == 'R') {
      Serial2.println("R");
    } else if (command == 'S') {
      initData();
      Serial2.println("S");
      startFlag = true;
    } else if (command == 'E') {
      Serial2.println("E");
      startFlag = false;
    } else if (command == 'B') {
      Serial.println(data);
      Serial3.println(data);
    } else {
      Serial.println(data);
    }
  }

  // 자전거 속도
  if (Serial2.available()) {
    bi_buf[bi_i] = Serial2.read();
    //    Serial.print(bi_i);
    //    Serial.print(":");
    //    Serial.print(bi_buf[bi_i], DEC);
    //    Serial.print("  ");
    if (bi_buf[bi_i] == 10) {
      String content(bi_buf);
      if (content.charAt(0) == 'S') {
        bike_speed = content.substring(1, content.indexOf('D')).toFloat();
        bike_distance = content.substring(content.indexOf('D') + 1, content.length() - 2).toFloat();
      } else if (content.charAt(0) == 'R') {
        Serial.print(content);
        MBT.print(content);
      }
      clearBIBuf();
      bi_i = -1;
    }
    bi_i++;
  }

  // 심박수
  if (Serial3.available()) {
    hr_buf[hr_i] = Serial3.read();
    if (hr_buf[hr_i] == 10) {
      String content(hr_buf);
      if (content.charAt(0) == 'H') {
        String hr = content.substring(1, content.length() - 2);
        heart_rate = hr.toInt();
      } else {
        Serial.print(content);
        MBT.print(content);
      }
      clearHRBuf();
      hr_i = -1;
    }
    hr_i++;
  }

  if (startFlag) {
    outData();
    getHandleAngle();
  }

}

void outData() {
  next_time = millis();
  if ((next_time - prev_time) > SEND_TIME) {
    String line = String(bike_speed) + "," + String(bike_distance) + "," + String(handle_angle) + "," + String(heart_rate);
    MBT.println(line);
    Serial.println(line);
    prev_time = next_time;
    checkBikeSpeed();
  }
}

void initData() {
  prev_time = millis();

  handle_angle = 0;
  bike_speed = 0.0;
  bike_distance = 0.0;
  heart_rate = 0;

  clearHRBuf();
  clearBIBuf();



  Serial2.println("I");

  mpu.getMotion9(&AcX, &AcY, &AcZ, &GyX, &GyY, &GyZ, &MgX, &MgY, &MgZ);
  sValue = (atan2(MgY, MgX) * 180 / PI + 270) * 2;
}

void checkBikeSpeed() {
  if (prevSpeed == bike_speed)
    sameSpeedFlag++;
  else {
    sameSpeedFlag = 0;
    prevSpeed = bike_speed;
  }

  if (sameSpeedFlag > MAX_SAME_SPEED) {
    bike_speed = 0;
    sameSpeedFlag = 0;
  }
}

void getHandleAngle() {
  mpu.getMotion9(&AcX, &AcY, &AcZ, &GyX, &GyY, &GyZ, &MgX, &MgY, &MgZ);
  double yaw = (atan2(MgY, MgX) * 180 / PI + 270) * 2;

  yaw -= sValue;

  if (yaw > MAX_ANGLE) yaw = MAX_ANGLE;
  else if (yaw < -MAX_ANGLE) yaw = -MAX_ANGLE;

  handle_angle = ((int) (yaw / 5)) * 5;
}

void getBikeSpeedAndDistance() {

}

void clearHRBuf() {
  for (; hr_i > -1; hr_i--) hr_buf[hr_i] = 0;
  hr_i = 0;
}

void clearBIBuf() {
  for (; bi_i > -1; bi_i--) bi_buf[bi_i] = 0;
  bi_i = 0;
}

