package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents a fully loaded base entity
 *
 * @author vallee
 */
public interface IBaseEntityDTO extends IBaseEntityIDDTO {

  /**
   * @return the name in current locale catalog
   */
  public String getBaseEntityName();

  /**
   * @return created track (read-only)
   */
  public ISimpleTrackingDTO getCreatedTrack();

  /**
   * @return last modified track (read-only)
   */
  public ISimpleTrackingDTO getLastModifiedTrack();

  /**
   * @return the source catalog (read-only)
   */
  public String getSourceCatalogCode();

  /**
   * @return the origin base entity IID of clone (read-only)
   */
  public long getOriginBaseEntityIID();

  /**
   * @return the set of value records involved by the entity
   */
  public Set<IPropertyRecordDTO> getPropertyRecords();

  /**
   * @param records overwritten value records
   */
  public void setPropertyRecords(IPropertyRecordDTO... records);

  /**
   * @param propertyIID the property identifier
   * @return the value record identified by propertyIID or null
   */
  public IPropertyRecordDTO getPropertyRecord(long propertyIID);

  /**
   * @return the entity extension part as JSON content
   */
  public IJSONContent getEntityExtension();

  /**
   * @param jsonContent overwritten extension part
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void setEntityExtension(String jsonContent) throws CSFormatException;

  /**
   * @return the top parent IID when this entity is an embedded part at any level
   */
  public long getTopParentIID();

  /**
   * @return the parent IID when this entity is an embedded part
   */
  public long getParentIID();

  /**
   * @return the type of parent-child relationship when this entity is an embedded part
   */
  public EmbeddedType getEmbeddedType();

  /**
   * @param type an embedded type of relation
   * @param baseEntityIID overwritten parent IID
   */
  public void setParentIID(EmbeddedType type, long baseEntityIID);

  /**
   * @return the children IIDs when this entity has embedded parts
   */
  public Set<Long> getChildrenIIDs();

  /**
   * @return the parent ID when this entity is an embedded part
   */
  public String getParentID();


  /**
   * @return the top parent ID when this entity is an embedded part
   */
  public String getTopParentID();

  /**
   * @return the isExpired to check whether asset is expired or not
   */
  public boolean isExpired();

  /**
   * set isExpired according to the asset expiry status 
   */
  public void setExpired(boolean isExpired);
 
  /**
   * @return the isDuplicate to check whether asset is duplicate or not
   */
  public boolean isDuplicate();
  
  /**
   * set isDuplicate according to the asset Duplicate status
   */
  public void setDuplicate(boolean isDuplicate);
  
  /**This method will give all locale ids in which content is available.
   * @return list of locale ids.
   */
  public List<String> getLocaleIds();
  
  /**
   * @param languageCodes locale ids to be set for a content.
   */
  public void setLocaleIds(List<String> localeIds);
  
  /**
   * 
   * @return weather entity is cloned or not
   */
  public boolean isClone();
  
  /**
   * @return the end point (read-only)
   */
  public String getEndpointCode();

  public void setChildren(EmbeddedType type, IBaseEntityIDDTO... children);

  public Set<IBaseEntityChildrenDTO> getChildren();

  /**
   * 
   * @return the isMerged to check whether entity is merged into goldenRecord or not
   */
  public boolean isMerged();
  
  /**
   * 
   * @param isMerged set true if entity is merged into baseEntity
   */
  public void setMerged(boolean isMerged);

  /**
   * 
   * @param sourceOrganizationCode
   */
  public void setSourceOrganizationCode(String sourceOrganizationCode);

  
  public void setChildrenFromEntity(EmbeddedType type, Collection<IBaseEntityDTO> childrenDTOs);

}
