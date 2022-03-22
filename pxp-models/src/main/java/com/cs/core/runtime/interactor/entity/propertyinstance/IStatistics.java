package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceUniquenessRuleInfo;
import java.util.List;

public interface IStatistics extends IEntity {
  
  public static final String KPI_ID               = "kpiId";
  public static final String ACCURACY             = "accuracy";
  public static final String COMPLETENESS         = "completeness";
  public static final String CONFORMITY           = "conformity";
  public static final String UNIQUENESS           = "uniqueness";
  public static final String UNIQUENESS_RULE_INFO = "uniqunessRuleInfo";
  public static final String IS_DUPLICATE         = "isDuplicate";
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
  
  public Double getAccuracy();
  
  public void setAccuracy(Double accuracy);
  
  public Double getCompleteness();
  
  public void setCompleteness(Double completeness);
  
  public Double getConformity();
  
  public void setConformity(Double confirmity);
  
  public Double getUniqueness();
  
  public void setUniqueness(Double Uniqueness);
  
  public List<IKlassInstanceUniquenessRuleInfo> getUniqunessRuleInfo();
  
  public void setUniqunessRuleInfo(List<IKlassInstanceUniquenessRuleInfo> uniquenessRuleInfo);
  
  public Integer getIsDuplicate();
  
  public void setIsDuplicate(Integer isDuplicate);
}
