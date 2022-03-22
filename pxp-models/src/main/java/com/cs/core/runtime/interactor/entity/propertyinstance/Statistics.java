package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceUniquenessRuleInfo;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceUniquenessRuleInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class Statistics implements IStatistics {
  
  private static final long                        serialVersionUID  = 1L;
  
  protected String                                 id;
  protected String                                 kpiId;
  protected Double                                 accuracy = 0.0;
  protected Double                                 completeness = 0.0;
  protected Double                                 uniqueness = 0.0;
  protected Double                                 conformity = 0.0;
  protected List<IKlassInstanceUniquenessRuleInfo> uniqunessRuleInfo = new ArrayList<>();
  protected Long                                   versionId;
  protected Long                                   versionTimestamp;
  protected String                                 lastModifiedBy;
  protected Integer                                isDuplicate;
  
  @Override
  public Double getAccuracy()
  {
    return accuracy;
  }
  
  @Override
  public void setAccuracy(Double accuracy)
  {
    this.accuracy = accuracy;
  }
  
  @Override
  public Double getCompleteness()
  {
    return completeness;
  }
  
  @Override
  public void setCompleteness(Double completeness)
  {
    this.completeness = completeness;
  }
  
  @Override
  public Double getUniqueness()
  {
    return uniqueness;
  }
  
  @Override
  public void setUniqueness(Double uniqueness)
  {
    this.uniqueness = uniqueness;
  }
  
  @Override
  public Double getConformity()
  {
    return conformity;
  }
  
  @Override
  public void setConformity(Double conformity)
  {
    this.conformity = conformity;
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
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  @Override
  public List<IKlassInstanceUniquenessRuleInfo> getUniqunessRuleInfo()
  {
    return uniqunessRuleInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceUniquenessRuleInfo.class)
  public void setUniqunessRuleInfo(List<IKlassInstanceUniquenessRuleInfo> uniquenessRuleInfo)
  {
    this.uniqunessRuleInfo = uniquenessRuleInfo;
  }
  
  @Override
  public Integer getIsDuplicate()
  {
    return isDuplicate;
  }
  
  @Override
  public void setIsDuplicate(Integer isDuplicate)
  {
    this.isDuplicate = isDuplicate;
  }
}
