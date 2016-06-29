package com.salyert.swarathesh.inventoryappproject11;


public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
