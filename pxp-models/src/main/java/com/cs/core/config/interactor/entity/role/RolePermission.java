package com.cs.core.config.interactor.entity.role;

import com.cs.core.config.interactor.entity.propertycollection.ISectionPermission;
import com.cs.core.config.interactor.entity.propertycollection.SectionPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RolePermission implements IRolePermission {
  
  private static final long                 serialVersionUID = 1L;
  
  protected Map<String, ISectionPermission> sectionPermission;
  
  protected List<String>                    visibleStructureViews;
  
  protected Long                            versionId;
  
  protected Long                            versionTimestamp;
  
  protected String                          lastModifiedBy;
  protected String                          code;
  
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
  public Map<String, ISectionPermission> getSectionPermission()
  {
    return sectionPermission;
  }
  
  @JsonDeserialize(contentAs = SectionPermission.class)
  @Override
  public void setSectionPermission(Map<String, ISectionPermission> sectionPermission)
  {
    this.sectionPermission = sectionPermission;
  }
  
  @Override
  public List<String> getVisibleStructureViews()
  {
    if (visibleStructureViews == null) {
      visibleStructureViews = new ArrayList<String>();
    }
    return visibleStructureViews;
  }
  
  @Override
  public void setVisibleStructureViews(List<String> allowedContentStructureViews)
  {
    this.visibleStructureViews = allowedContentStructureViews;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
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
