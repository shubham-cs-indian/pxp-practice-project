package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;

public class KlassInstanceTreeInformationModel implements IKlassInstanceTreeInformationModel {
  
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected String          name;
  
  protected String          owner;
  
  protected String          type;
  
  protected Integer         versionId;
  
  protected String          parentId;
  
  protected List<String>    klassInstanceIds;
  
  protected List<String>    setInstanceIds;
  
  protected Boolean         isFolder;
  
  public List<String> getSetInstanceIds()
  {
    return setInstanceIds;
  }
  
  public void setSetInstanceIds(List<String> setInstanceIds)
  {
    this.setInstanceIds = setInstanceIds;
  }
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getOwner()
  {
    return owner;
  }
  
  @Override
  public void setOwner(String owner)
  {
    this.owner = owner;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Integer getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Integer versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public Boolean getIsFolder()
  {
    return isFolder;
  }
  
  @Override
  public void setIsFolder(Boolean isFolder)
  {
    this.isFolder = isFolder;
  }
}
