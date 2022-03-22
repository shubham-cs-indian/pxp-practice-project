package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.util.Set;

/**
 * Contextual data attached to an entity or to a property record
 *
 * @author vallee
 */
public interface IContextualDataDTO extends IRootDTO {

  /**
   * @return the internal database identified
   */
  public long getContextualObjectIID();

  /**
   * @return the context code referred by this object or empty context object
   */
  public String getContextCode();
  
  /**
   * @param status overwritten allow duplicate status on related context information
   */
  public void setAllowDuplicate( boolean status);
  
  /**
   * @return the allow duplicate status on related context information
   */
  public boolean getAllowDuplicate();

  /**
   * @return the context referred by this object or empty context object
   */
  public IContextDTO getContext();

  /**
   * @return context startTime
   */
  public long getContextStartTime();

  /**
   * @param time overwritten context startTime
   */
  public void setContextStartTime(long time);

  /**
   * @return context endTime
   */
  public long getContextEndTime();

  /**
   * @param time overwritten context endTime
   */
  public void setContextEndTime(long time);

  /**
   * @return the set of tags involved by the context
   */
  public Set<ITagDTO> getContextTagValues();

  /**
   * @param tags overwritten context tags
   */
  public void setContextTagValues(ITagDTO... tags);

  /**
   * @return the IID of the attached entities
   */
  public Set<Long> getLinkedBaseEntityIIDs();

  /**
   * @param linkedBaseEntityIIDs overwritten set of attached entities
   */
  public void setLinkedBaseEntityIIDs(Long... linkedBaseEntityIIDs);

}
