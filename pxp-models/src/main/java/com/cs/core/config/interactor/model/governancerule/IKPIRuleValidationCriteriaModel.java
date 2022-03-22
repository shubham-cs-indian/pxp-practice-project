package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKPIRuleValidationCriteriaModel extends IModel {
  
  public static final String IS_ACCURACY_VALID     = "isAccuracyValid";
  public static final String IS_COMPLETENESS_VALID = "isCompletenessValid";
  public static final String IS_CONFORMITY_VALID   = "isConformityValid";
  public static final String IS_UNIQUENESS_VALID   = "isUniquenessValid";
  
  public Boolean getIsAccuracyValid();
  
  public void setIsAccuracyValid(Boolean isAccuracyValid);
  
  public Boolean getIsCompletenessValid();
  
  public void setIsCompletenessValid(Boolean isCompletenessValid);
  
  public Boolean getIsConformityValid();
  
  public void setIsConformityValid(Boolean isConformityValid);
  
  public Boolean getIsUniquenessValid();
  
  public void setIsUniquenessValid(Boolean isUniquenessValid);
}
