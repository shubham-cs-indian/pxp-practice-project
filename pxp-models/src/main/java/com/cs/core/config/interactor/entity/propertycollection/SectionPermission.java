package com.cs.core.config.interactor.entity.propertycollection;

import java.util.ArrayList;
import java.util.List;

public class SectionPermission implements ISectionPermission {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected Boolean         isCollapsed      = false;
  
  protected Boolean         isHidden         = false;
  
  protected List<String>    disabledElements = new ArrayList<String>();
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          lastModifiedBy;
  protected String          code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Boolean getIsCollapsed()
  {
    return isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
  }
  
  @Override
  public Boolean getIsHidden()
  {
    return isHidden;
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    this.isHidden = isHidden;
  }
  
  @Override
  public List<String> getDisabledElements()
  {
    return disabledElements;
  }
  
  @Override
  public void setDisabledElements(List<String> disabledElements)
  {
    this.disabledElements = disabledElements;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
}
