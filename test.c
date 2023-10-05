#include<stdio.h> 
#include <wiringPi.h> 
#define LO 26
#define L1 27
#define L2 28
#define L3 29

int main(void){
    pinMode (LO, OUTPUT); 
    pinMode (L1, OUTPUT) ; 
    pinMode (L2, OUTPUT) ; 
    pinMode (L3, OUTPUT);
    int n, k=0, LO, L1, L2, L3;
    for (n=0; n<4; n++) {
        k = n&OxF;
        LO=(k&(n+1))>>n;
        L1=(k& (n+1))>>n;
        L2=(k&(n+1))>>n;
        L3=(k& (n+1))>>n;
        
        
        //Ln = (k>>n)80x01;
        digitalwrite(L*n,1);
        delay(100);
        digitalWrite(L*n, 0);
        delay(100);
    }
}


#include <stdio.h>
#include <wiringPi.h>

#define LO 26
#define L1 27
#define L2 28
#define L3 29

int main(void) {
    wiringPiSetup(); // wiringPi 라이브러리 초기화

    pinMode(LO, OUTPUT);
    pinMode(L1, OUTPUT);
    pinMode(L2, OUTPUT);
    pinMode(L3, OUTPUT);

    int n, k, L0, L1, L2, L3;

    for (n = 0; n < 16; n++) {
        k = n;
        L0 = k & 1;
        L1 = (k >> 1) & 1;
        L2 = (k >> 2) & 1;
        L3 = (k >> 3) & 1;

        digitalWrite(LO, L0);
        digitalWrite(L1, L1);
        digitalWrite(L2, L2);
        digitalWrite(L3, L3);

        delay(1000); // 1초 동안 대기
    }

    return 0;
}
