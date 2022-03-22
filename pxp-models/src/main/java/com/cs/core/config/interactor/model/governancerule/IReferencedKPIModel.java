package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IThreshold;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IReferencedKPIModel extends IModel {
  
  public static final String LABEL                        = "label";
  public static final String THRESHOLDS                   = "thresholds";
  public static final String KPI_RULE_VALIDATION_CRITERIA = "kpiRuleValidationCriteria";
  public static final String CODE                         = "code";
  public static final String ACCURACYID                   = "accuracyId";
  public static final String CONFORMITYID                 = "conformityId";
  public static final String COMPLETENESSID               = "completenessId";
  public static final String UNIQUENESSID                 = "uniquenessId";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Map<String, IThreshold> getThresholds();
  
  public void setThresholds(Map<String, IThreshold> thresholds);
  
  public IKPIRuleValidationCriteriaModel getKpiRuleValidationCriteria();
  
  public void setKpiRuleValidationCriteria(
      IKPIRuleValidationCriteriaModel kpiRuleValidationCriteria);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getAccuracyId();
  
  public void setAccuracyId(String accuracyId);
  
  public String getConformityId();
  
  public void setConformityId(String conformityId);
  
  public String getCompletenessId();
  
  public void setCompletenessId(String completenessId);
  
  public String getUniquenessId();
  
  public void setUniquenessId(String uniquenessId);
  
}
