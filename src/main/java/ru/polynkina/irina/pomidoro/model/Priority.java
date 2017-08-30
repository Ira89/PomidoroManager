package ru.polynkina.irina.pomidoro.model;

public enum Priority {

    A, B, C, D;

    public static Priority getValueByIndex(int index) {
        switch(index) {
            case 0: return A;
            case 1: return B;
            case 2: return C;
            default: return D;
        }
    }
}
