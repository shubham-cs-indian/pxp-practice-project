package com.cs.core.config.interactor.entity.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KlassStructureSectionSetting implements IKlassStructureSectionSetting {
  
  private static final long                                   serialVersionUID = 1L;
  
  protected String                                            id;
  
  protected Boolean                                           isVisible        = true;
  
  protected Boolean                                           isCollapsed      = false;
  
  protected Map<String, IKlassStructureSectionElementSetting> elements         = new HashMap<String, IKlassStructureSectionElementSetting>();
  
  protected Long                                              versionId;
  
  protected Long                                              versionTimestamp;
  
  protected String                                            lastModifiedBy;
  
  protected String                                            code;
  
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
  public Boolean getIsVisible()
  {
    return this.isVisible;
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    this.isVisible = isVisible;
  }
  
  @Override
  public Boolean getIsCollapsed()
  {
    return this.isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
  }
  
  @Override
  public Map<String, IKlassStructureSectionElementSetting> getElements()
  {
    return this.elements;
  }
  
  @JsonDeserialize(contentAs = KlassStructureSectionElementSetting.class)
  @Override
  public void setElements(Map<String, IKlassStructureSectionElementSetting> elements)
  {
    this.elements = elements;
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
}
