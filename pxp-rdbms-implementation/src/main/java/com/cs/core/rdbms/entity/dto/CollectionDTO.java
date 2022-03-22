package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTOBuilder;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * DTO representation of a Collection/Bookmark
 *
 * @author Kushal
 */
public class CollectionDTO extends RDBMSRootDTO implements ICollectionDTO {
  
  private static final long serialVersionUID     = 1L;
  
  private CollectionType    collectionType       = CollectionType.UNDEFINED;
  private String            collectionCode       = "";
  private long              parentIID            = 0L;
  private SimpleTrackingDTO created              = new SimpleTrackingDTO();
  private SimpleTrackingDTO lastModified         = new SimpleTrackingDTO();
  private JSONContent       searchCriteria;
  private String            catalogCode          = "";
  private String            organizationCode     = "";
  private boolean           isPublic;
  private Set<Long>   linkedBaseEntityIIDs = new HashSet<>();


  /**
   * Enabled default constructor
   */
  public CollectionDTO()
  {
  }
  /**
   * Value Constructor
   *
   * @param collectionType
   * @param CollectionCode
   * @param parentIID
   * @param created
   * @param lastModified
   * @param catalogCode
   * @param organizationCode
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public CollectionDTO( CollectionType collectionType, String collectionCode, String catalogCode,
      String organizationCode)  throws CSFormatException
  {
    this.collectionType = collectionType;
    this.collectionCode = collectionCode;
    this.catalogCode = catalogCode;
    this.organizationCode = organizationCode;
  }

  public CollectionDTO(IResultSetParser parser) throws SQLException, CSFormatException
  {
    super(parser.getLong("collectionIID"));
    this.collectionType = CollectionType.valueOf(parser.getInt("collectionType"));
    this.collectionCode = parser.getString("collectionCode");
    this.parentIID = parser.getLong("parentIID");
    this.catalogCode = parser.getString("catalogCode");
    created = new SimpleTrackingDTO(parser.getString("userName"),
        parser.getLong("createtime"));
    lastModified = new SimpleTrackingDTO(parser.getString("userName"),
        parser.getLong("lastModifiedTime"));
    String jsonSearchCriteria = parser.getStringFromJSON("searchCriteria").trim();
    this.searchCriteria = !jsonSearchCriteria.isEmpty() ? new JSONContent(jsonSearchCriteria)
        : new JSONContent();
    this.catalogCode = parser.getString("catalogCode");
    this.organizationCode = parser.getString("organizationCode");
    this.isPublic = parser.getBoolean("isPublic");
  }
  
  @Override
  public long getCollectionIID()
  {
    return getIID();
  }
  
  @Override
  public CollectionType getCollectionType()
  {
    return collectionType;
  }
  
  @Override
  public String getCollectionCode()
  {
    return collectionCode;
  }
  
  @Override
  public long getParentIID()
  {
    return parentIID;
  }
  
  public void setParentIID(Long parentIID)
  {
    this.parentIID = parentIID;
  }
  
  @Override
  public ISimpleTrackingDTO getCreatedTrack()
  {
    return created;
  }
  
  public void setCreatedTrack(ISimpleTrackingDTO created)
  {
    this.created.setWhen(created.getWhen());
    this.created.setWho(created.getWho());
  }
  
  @Override
  public ISimpleTrackingDTO getLastModifiedTrack()
  {
    return lastModified;
  }
  
  public void setLastModifiedTrack(ISimpleTrackingDTO modified)
  {
    this.lastModified.setWhen(modified.getWhen());
    this.lastModified.setWho(modified.getWho());
  }
  
  @Override
  public void setSearchCriteria(IJSONContent searchCriteria) throws CSFormatException
  {
    this.searchCriteria = (JSONContent) searchCriteria;
  }
  
  @Override
  public IJSONContent getSearchCriteria()
  {
    return searchCriteria;
  }
  
  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }
  
  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }
  
  @Override
  public Set<Long> getLinkedBaseEntityIIDs()
  {
    return linkedBaseEntityIIDs;
  }
  
  @Override
  public void setLinkedBaseEntityIIDs(Set<Long> linkedBaseEntityIIDs)
  {
    this.linkedBaseEntityIIDs.clear();
    this.linkedBaseEntityIIDs.addAll(linkedBaseEntityIIDs);
  }
  
  @Override
  public boolean getIsPublic()
  {
    return isPublic;
  }
  
  @Override
  public void setIsPublic(boolean isPublic)
  {
    this.isPublic = isPublic;
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    
  }
  
  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    
  }
  
	/**
	 * implementation of ICollectionDTOBuilder
	 * @author harsh.shivhare
	 *
	 */
  public static class CollectionDTOBuilder implements ICollectionDTOBuilder {
    
    private final CollectionDTO collectionDTO;
    
    /**
     * value constructor
     * 
     * @param collectionType
     * @param collectionCode
     * @param catalogCode
     * @param organizationCode
     * @throws CSFormatException
     */
    public CollectionDTOBuilder(CollectionType collectionType, String collectionCode, String catalogCode, String organizationCode)
        throws CSFormatException
    {
      collectionDTO = new CollectionDTO(collectionType, collectionCode, catalogCode, organizationCode);
    }
    
    @Override
    public ICollectionDTOBuilder parentIID(long parentIID)
    {
      collectionDTO.setParentIID(parentIID);
      return this;
    }
    
    @Override
    public ICollectionDTOBuilder searchCriteria(IJSONContent searchCriteria) throws CSFormatException
    {
      collectionDTO.setSearchCriteria(searchCriteria);
      return this;
    }
    
    @Override
    public ICollectionDTOBuilder isPublic(boolean isPublic)
    {
      collectionDTO.setIsPublic(isPublic);
      return this;
    }
    
    @Override
    public ICollectionDTOBuilder linkedBaseEntityIIDs(Set<Long> linkedBaseEntityIIDs)
    {
      collectionDTO.setLinkedBaseEntityIIDs(linkedBaseEntityIIDs);
      return this;
    }
    
    @Override
    public ICollectionDTOBuilder linkedBaseEntityIID(Long linkedBaseEntityIID)
    {
      collectionDTO.getLinkedBaseEntityIIDs().add(linkedBaseEntityIID);
      return this;
    }
    

    @Override
    public ICollectionDTO build()
    {
      return collectionDTO;
    }

    
  }

}
