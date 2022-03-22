package com.cs.core.config.interactor.entity.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KlassStructureSetting implements IKlassStructureSetting {
  
  private static final long                            serialVersionUID = 1L;
  
  protected String                                     id;
  
  protected Boolean                                    isEditable       = false;
  
  protected Boolean                                    showSections     = false;
  
  protected Map<String, IKlassStructureSectionSetting> sections         = new HashMap<String, IKlassStructureSectionSetting>();
  
  protected Long                                       versionId;
  
  protected Long                                       versionTimestamp;
  
  protected String                                     lastModifiedBy;
  protected String                                     code;
  
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
  public Boolean getIsEditable()
  {
    return this.isEditable;
  }
  
  @Override
  public void setIsEditable(Boolean isEditable)
  {
    this.isEditable = isEditable;
  }
  
  @Override
  public Boolean getShowSections()
  {
    return this.showSections;
  }
  
  @Override
  public void setShowSections(Boolean showSections)
  {
    this.showSections = showSections;
  }
  
  @Override
  public Map<String, IKlassStructureSectionSetting> getSections()
  {
    return this.sections;
  }
  
  @JsonDeserialize(contentAs = KlassStructureSectionSetting.class)
  @Override
  public void setSections(Map<String, IKlassStructureSectionSetting> sections)
  {
    this.sections = sections;
  }
}
