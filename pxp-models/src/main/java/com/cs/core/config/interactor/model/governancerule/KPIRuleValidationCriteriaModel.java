package com.cs.core.config.interactor.model.governancerule;

public class KPIRuleValidationCriteriaModel implements IKPIRuleValidationCriteriaModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isAccuracyValid;
  protected Boolean         isCompletenessValid;
  protected Boolean         isConformityValid;
  protected Boolean         isUniquenessValid;
  
  @Override
  public Boolean getIsAccuracyValid()
  {
    if (isAccuracyValid == null) {
      isAccuracyValid = false;
    }
    return isAccuracyValid;
  }
  
  @Override
  public void setIsAccuracyValid(Boolean isAccuracyValid)
  {
    this.isAccuracyValid = isAccuracyValid;
  }
  
  @Override
  public Boolean getIsCompletenessValid()
  {
    if (isCompletenessValid == null) {
      isCompletenessValid = false;
    }
    return isCompletenessValid;
  }
  
  @Override
  public void setIsCompletenessValid(Boolean isCompletenessValid)
  {
    this.isCompletenessValid = isCompletenessValid;
  }
  
  @Override
  public Boolean getIsConformityValid()
  {
    if (isConformityValid == null) {
      isConformityValid = false;
    }
    return isConformityValid;
  }
  
  @Override
  public void setIsConformityValid(Boolean isConformityValid)
  {
    this.isConformityValid = isConformityValid;
  }
  
  @Override
  public Boolean getIsUniquenessValid()
  {
    if (isUniquenessValid == null) {
      isUniquenessValid = false;
    }
    return isUniquenessValid;
  }
  
  @Override
  public void setIsUniquenessValid(Boolean isUniquenessValid)
  {
    this.isUniquenessValid = isUniquenessValid;
  }
}
