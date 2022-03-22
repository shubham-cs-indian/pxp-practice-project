package com.cs.core.eventqueue.idto;

import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;

import static com.cs.core.eventqueue.idto.IEventDTO.EventTopic.*;

/**
 * DTO for PXP event information
 *
 * @author vallee
 */
public interface IEventDTO extends IObjectTrackingDTO {

  /**
   * @return the event IID
   */
  public default long getEventIID() {
    return getTrackIID();
  }

  /**
   * @return the service topic of this event
   */
  public default EventTopic getTopic() {
    return getType().getTopic();
  }

  /**
   * @return the service expected for this event
   */
  public EventType getType();

  /**
   * @return the timestamp of consumption
   */
  public long getConsumedTime();

  /**
   * @return the timestamp of flag
   */
  public long getFlaggedTime();

  /**
   * @return the localeID for which the event is posted
   */
  public String getLocaleID();

  String getTransactionId();

  /**
   * Category of services
   */
  public enum EventTopic {
    UNDEFINED, GENERIC, ENTITY, CHILDREN, CLASSIFIERS, RELATIONS, COUPLING;
  }

  /**
   * Services with type of service
   */
  public enum EventType { 

    UNDEFINED(EventTopic.UNDEFINED), GENERIC_EVENT(GENERIC),
    OBJECT_CREATION(ENTITY), OBJECT_UPDATE(ENTITY), OBJECT_DELETE(ENTITY),
    CHILDREN_ADDED(CHILDREN), CHILDREN_REMOVED(CHILDREN), OBJECT_TRANSFER(ENTITY),
    CLASSIFIER_ADDED(CLASSIFIERS), CLASSIFIER_REMOVED(CLASSIFIERS),
    RELATION_CREATION(RELATIONS), RELATION_DELETE(RELATIONS), SIDE_RELATION_CREATION(RELATIONS),
    SIDE_RELATION_DELETE(RELATIONS), CALCULATION_SOURCE(COUPLING), RULE_EVALUATION_SOURCE(COUPLING), 
    COUPLING_SOURCE(COUPLING), TRANSLATION_ADDED(ENTITY), TRANSLATION_REMOVED(ENTITY),
    END_OF_TRANSACTION(ENTITY), ELASTIC_UPDATE(ENTITY), ELASTIC_TRANSLATION_DELETE(ENTITY), ELASTIC_DELETE(ENTITY);

    private static final EventType[] values = values();
    private final EventTopic topic;

    EventType(EventTopic topic) {
      this.topic = topic;
    }

    public static EventType valueOf(int ordinal) {
      return values[ordinal];
    }

    public EventTopic getTopic() {
      return topic;
    }
  }
}
