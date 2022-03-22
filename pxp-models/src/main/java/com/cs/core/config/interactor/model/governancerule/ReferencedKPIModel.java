package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IThreshold;
import com.cs.core.config.interactor.entity.governancerule.Threshold;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ReferencedKPIModel implements IReferencedKPIModel {
   
  private static final long                 serialVersionUID = 1L;
  protected String                          label;
  protected Map<String, IThreshold>         thresholds;
  protected IKPIRuleValidationCriteriaModel kpiRuleValidationCriteria;
  protected String                          code;
  protected String                          accuracyId;
  protected String                          conformityId;
  protected String                          completenessId;
  protected String                          uniquenessId;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Map<String, IThreshold> getThresholds()
  {
    return thresholds;
  }
  
  @JsonDeserialize(contentAs = Threshold.class)
  @Override
  public void setThresholds(Map<String, IThreshold> thresholds)
  {
    this.thresholds = thresholds;
  }
  
  @Override
  public IKPIRuleValidationCriteriaModel getKpiRuleValidationCriteria()
  {
    return kpiRuleValidationCriteria;
  }
  
  @Override
  @JsonDeserialize(as = KPIRuleValidationCriteriaModel.class)
  public void setKpiRuleValidationCriteria(
      IKPIRuleValidationCriteriaModel kpiRuleValidationCriteria)
  {
    this.kpiRuleValidationCriteria = kpiRuleValidationCriteria;
  }
  
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
  public String getAccuracyId()
  {
    return accuracyId;
  }

  @Override
  public void setAccuracyId(String accuracyId)
  {
    this.accuracyId = accuracyId;
  }

  @Override
  public String getConformityId()
  {
    return conformityId;
  }

  @Override
  public void setConformityId(String conformityId)
  {
    this.conformityId = conformityId;
  }

  @Override
  public String getCompletenessId()
  {
    return completenessId;
  }

  @Override
  public void setCompletenessId(String completenessId)
  {
    this.completenessId = completenessId;
  }

  @Override
  public String getUniquenessId()
  {
    return uniquenessId;
  }

  @Override
  public void setUniquenessId(String uniquenessId)
  {
    this.uniquenessId = uniquenessId;
  }
}
