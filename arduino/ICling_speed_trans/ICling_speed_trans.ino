#define MAGNETIC 2

char buf[100];
int i = 0;

int CurrentSwitch = HIGH;
int PrevSwitch = HIGH;
int DigitalValue = 0;
float WheelDistance = 2500.0;  // 한바퀴 이동 거리
float Distance = 0.0;
float Speed = 0.0;
float MaxSpeed = 0.0;
unsigned long CurrentTime = 0;
unsigned long PrevTime = 0;

double bike_speed;
double bike_distance;

boolean flag = false;

void setup() {
  Serial.begin(9600);
  pinMode(MAGNETIC, INPUT_PULLUP);
  initData();
}

void loop() {
  if (Serial.available()) {
    buf[i] = Serial.read();
//    Serial.print(i);
//    Serial.print(":");
//    Serial.print(buf[i], DEC);
//    Serial.print("  ");
    if (buf[i] == 10) {
      i = -1;
      String message(buf);
      if (message.charAt(0) == 'W') {
        WheelDistance = message.substring(1, 3).toInt() * 100.0;
      } else if(message.charAt(0) == 'R') {
        Serial.print("R");
        Serial.println(String(WheelDistance));  
      } else if (message.charAt(0) == 'I') {
        initData();
      } else if (message.charAt(0) == 'S') {
        initData();
        flag = true;
      } else if (message.charAt(0) == 'E') {
        flag = false;
      }
      clearBuf();
    }
    i++;
  }
  
  if (flag) {
    DigitalValue = digitalRead(MAGNETIC);
    if (DigitalValue == 1) {  // 센서신호 검출 및 판단
      CurrentSwitch = HIGH;
    }
    else {
      CurrentSwitch = LOW;
    }

    if (CurrentSwitch == LOW) {
      if (PrevSwitch == HIGH) {
        CurrentTime = millis();
        if (PrevTime > 0) {
          float bike_radius = WheelDistance / 1000.0;
          Speed = (WheelDistance / 1000000.0) / ((CurrentTime - PrevTime) / 1000.0) * 3600.0;
          if (Speed < 40) {
            if (Speed > MaxSpeed) MaxSpeed = Speed;
            Distance += bike_radius;

            bike_speed = Speed;
            bike_distance = Distance / 1000.0;

            Serial.print("S");
            Serial.print(bike_speed);
            Serial.print("D");
            Serial.println(bike_distance);
          }
        }
        PrevSwitch = LOW;
        PrevTime = CurrentTime;
      }
    }
    else {
      PrevSwitch = HIGH;
    }
  }
}

void initData() {
  Distance = 0;
  bike_speed = 0;
  bike_distance = 0;
}

void clearBuf() {
  for (int i = 0; i < 100; i++) {
    if (buf[i] == 0) break;
    buf[i] = 0;
  }
}


