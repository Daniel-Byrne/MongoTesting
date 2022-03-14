package com.daniel.mongo.recorder;

public interface RecordableAction {
    
    public String name();
    
    public void run();
}
