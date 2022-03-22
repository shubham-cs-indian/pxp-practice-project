package com.cs.core.rdbms.revision.idto;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IUserDTO;

import java.util.Map;

/**
 * Object revision with Time line information
 *
 * @author vallee
 */
public interface IObjectRevisionDTO extends IObjectTrackingDTO {

  /**
   * @return the created date
   */
  public default long getCreated() {
    return getWhen();
  }

  ;
  
  /**
   * @return the current revision number of this time line
   */
  public int getRevisionNo();

  /**
   * Timeline data consists in JSON information structures originating from a series of Object Tracking Data The CSE contents represent
   * filtered information obtained from tracking data
   *
   * @return the revision data classified by Change Category
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Map<ChangeCategory, ICSEList> getTimelines() throws CSFormatException;

  /**
   * @return the user who originated this revision
   */
  public IUserDTO getAuthor();

  /**
   * @return the user comment attached to the revision time line
   */
  public String getRevisionComment();

  /**
   * @return the revision time line content in the form of a JSON map
   */
  public IJSONContent getRevisionTimeline();

  /**
   * @return the object archive
   */
  public IJSONContent getObjectArchive();

  /**
   * @return the object archive as BaseEntity DTO when applicable
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IBaseEntityDTO getBaseEntityDTOArchive() throws CSFormatException;
  
  public IBaseEntityDTO getBaseEntityDTOArchive(String objectArchive) throws CSFormatException;

  /*public enum RevisionEvent
  {
    
    UNDEFINED, ATTRIBUTES, TAGS, TAXONOMY_ADDED, TAXONOMY_REMOVED, NATURE_CLASS, RELATIONS,
    CHILDREN_ADDED, CHILDREN_REMOVED, LIFE_CYCLE, LISTING;
    
    private static final RevisionEvent[] values = values();
    
    public static RevisionEvent valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }*/
}
