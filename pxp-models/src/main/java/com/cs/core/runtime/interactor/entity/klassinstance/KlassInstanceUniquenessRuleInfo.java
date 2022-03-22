package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

public class KlassInstanceUniquenessRuleInfo implements IKlassInstanceUniquenessRuleInfo {
  
  private static final long      serialVersionUID = 1L;
  protected String               id;
  protected Long                 versionId;
  protected Long                 versionTimeStamp;
  protected String               lastModifiedBy;
  protected String               ruleId;
  protected List<IIdAndBaseType> klassInstanceIdBasetypeList;
  
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
    return versionTimeStamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimeStamp = versionTimestamp;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public List<IIdAndBaseType> getKlassInstanceIdBasetypeList()
  {
    return klassInstanceIdBasetypeList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setKlassInstanceIdBasetypeList(List<IIdAndBaseType> klassInstanceIdBasetypeList)
  {
    this.klassInstanceIdBasetypeList = klassInstanceIdBasetypeList;
  }
}
