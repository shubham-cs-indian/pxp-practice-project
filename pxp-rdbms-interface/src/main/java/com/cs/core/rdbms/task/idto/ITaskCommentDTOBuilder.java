package com.cs.core.rdbms.task.idto;

import java.util.Set;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

  /**
   * builder class for task comment
   *
   * @author Janak.Gurme
   */
  public interface ITaskCommentDTOBuilder extends IRootDTOBuilder<ITaskCommentDTO>{

    /**
     * @param time
     * @return ITaskCommentDTOBuilder
     */
    public ITaskCommentDTOBuilder time(long time);

    /**
     * @param attachments
     * @return
     */
    public ITaskCommentDTOBuilder attachments(Set<Long> attachments);

    /**
     * @param attachment
     * @return
     */
    public ITaskCommentDTOBuilder attachment(Long attachment);

    /**
     * @param text
     * @return
     */
    public ITaskCommentDTOBuilder text(String text);

    /**
     * @param userName
     * @return
     */
    public ITaskCommentDTOBuilder userName(String userName);

  }
