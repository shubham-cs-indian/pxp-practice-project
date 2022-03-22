package com.cs.core.rdbms.entity.idto;

import java.util.Set;

import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * Represents a Collection/Bookmark information
 *
 * @author Kushal
 */
public interface ICollectionDTO extends IRootDTO {
  
  /**
   * @return the object RDBMS IID
   */
  public long getCollectionIID();
  
  /**
   * @return the Collection type (read-only)
   */
  public CollectionType getCollectionType();
  
  /**
   * @return the Collection code
   */
  public String getCollectionCode();
  
  /**
   * @return the parent IID when this entity is child of another Collection
   */
  public long getParentIID();
  
  /**
   * @return created track (read-only)
   */
  public ISimpleTrackingDTO getCreatedTrack();
  
  /**
   * @return last modified track (read-only)
   */
  public ISimpleTrackingDTO getLastModifiedTrack();
  
  /**
   * @param searchCriteria having search and filter related info for Bookmarks
   */
  public void setSearchCriteria(IJSONContent searchCriteria) throws CSFormatException;
  
  /**
   * @return the searchCriteria of Bookmarks
   */
  public IJSONContent getSearchCriteria();
  
  /**
   * @return the code of the catalog or DI
   */
  public String getCatalogCode();
  
  /**
   * @return whether Collection/Bookmark is public or private
   */
  public boolean getIsPublic();
  
  /**
   * @param isPublic
   */
  public void setIsPublic(boolean isPublic);
  
  /**
   * @return organization code of collection
   */
  public String getOrganizationCode();
  
  /**
   * @return the IID of the entities linked to this Collection
   */
  public Set<Long> getLinkedBaseEntityIIDs();
  
  /**
   * @param linkedBaseEntityIIDs overwritten set of entities in Collections
   */
  public void setLinkedBaseEntityIIDs(Set<Long> linkedBaseEntityIIDs);
   

  /**
   * Constant for Collection type
   */
  public enum CollectionType
  {
    
    UNDEFINED, staticCollection, dynamicCollection;
    
    private static final CollectionType[] values = values();
    
    public static CollectionType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }
}
