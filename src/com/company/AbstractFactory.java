package com.company;

public interface AbstractFactory {
    Fiz_L createFiz();
    Fiz_L createFiz(float x, float y);
    Yur_L createYur();
    Yur_L createYur(float x, float y);
}
