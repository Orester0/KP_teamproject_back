package org.example.securitysystem.model.db_models;


import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

import java.time.LocalDateTime;

@StaticMetamodel(EventLog.class)
public class EventLog_ {

    public static volatile SingularAttribute<EventLog, SessionDB> session;
    public static volatile SingularAttribute<EventLog, FloorDB> floor;
    public static volatile SingularAttribute<EventLog, RoomDB> room;
    public static volatile SingularAttribute<EventLog, SensorDB> sensor;
    public static volatile SingularAttribute<EventLog, LocalDateTime> startTime;

}

