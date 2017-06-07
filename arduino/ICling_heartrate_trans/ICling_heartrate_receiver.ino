#include <SoftwareSerial.h>
#define AT_MODE 1
#define HR_MODE 2

SoftwareSerial BLE(2, 3);
char sri_buf[100];
char ble_buf[100];
int i = 0;
int b_i = 0;
int mode = 1;

void setup() {
  Serial.begin(9600);
  BLE.begin(9600);
  clearBufs();
}

void loop() {
  if (Serial.available()) {
    sri_buf[i] = Serial.read();
    if (sri_buf[i] == 10) {
      i = -1;
      checkCommand();
    }
    i++;
  }

  BLE.listen();
  if (mode == AT_MODE) atMode();
  else hrMode();
}

void atMode() {
  if (BLE.available()) {
    String content = BLE.readString();
    char connectionCheckBit = content.charAt(content.length() - 1);
    if (connectionCheckBit == 78) {
      mode = HR_MODE;
      Serial.println("Connection Success");
    } else if (connectionCheckBit == 70) {
      Serial.println("Connection Failed");
    } else {
      Serial.println(content);
    }
    //    for(int i = 0; i < content.length(); i++) {
    //      char w = content.charAt(i);
    //      Serial.print(w);
    //      Serial.print("[");
    //      Serial.print(w, DEC);
    //      Serial.print("]  ");
    //    }
    //    Serial.println();
  }
}

void hrMode() {
  if (BLE.available()) {
    ble_buf[b_i] = BLE.read();
    
    if (ble_buf[b_i] == 84) {
      mode = AT_MODE;
      Serial.println("Connection Lost");
      b_i = -1;
      
      clearBleBuf();
      return;
    }
    
    if (ble_buf[b_i] == 10) {
      b_i = -1;
      String hr(ble_buf);
      hr = hr.substring(0, hr.length() - 2);
      hr = String("H") + hr;
      Serial.println(hr);

      clearBleBuf();
    }
    
    b_i++;
  }
}

void checkCommand() {
  String content(sri_buf);
  char command = sri_buf[0];
  if (command == 'B') {
    char bleCommand = sri_buf[1];
    String cmd;

    if (bleCommand == 'H') cmd = String("AT+CON") + content.substring(2, 14);
    else cmd = content.substring(1, content.length() - 2);

    sendAT(cmd);
  }
}

void sendAT(String msg) {
  for (int i = 0; i < msg.length(); i++) {
    char w = msg.charAt(i);
    BLE.write(w);
  }
}

void clearBufs() {
  clearSriBuf();
  clearBleBuf();
}

void clearSriBuf() {
  for (int i = 0; i < 100; i++) {
    sri_buf[i] = 0;
  }
}

void clearBleBuf() {
  for (int i = 0; i < 100; i++) {
    ble_buf[i] = 0;
  }
}

