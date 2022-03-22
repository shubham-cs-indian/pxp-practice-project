package com.cs.core.rdbms.task.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.util.Set;

/**
 * Task comment DTO is comment attached to the task with user, time, attachment and message.
 *
 * @author Janak.Gurme
 */
public interface ITaskCommentDTO extends ISimpleDTO {

  /**
   * @return comment posted time
   */
  public long getTime();

  /**
   * @return the attachment identifiers of this comment
   */
  public Set<Long> getAttachments();

  /**
   * @return comment text for task
   */
  public String getText();

  /**
   * @return user name
   */
  public String getUserName();
}
