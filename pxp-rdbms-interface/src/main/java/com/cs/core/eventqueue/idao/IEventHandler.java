package com.cs.core.eventqueue.idao;

import com.cs.core.eventqueue.idto.IEventDTO;

/**
 * Handler for event queue notification
 *
 * @author vallee
 */
public interface IEventHandler extends Runnable {

  /**
   * Set the event as parameter before running this handler
   *
   * @param event
   */
  public void setEvent(IEventDTO event);
}
