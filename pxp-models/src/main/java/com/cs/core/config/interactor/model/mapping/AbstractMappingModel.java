package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigModelForBoarding;
import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractMappingModel implements IAbstractMappingModel {
  
  private static final long                       serialVersionUID = 1L;
  
  protected List<IConfigRuleClassMappingModel>    classMappings    = new ArrayList<>();
  protected List<IConfigRuleTaxonomyMappingModel> taxonomyMappings = new ArrayList<>();
  protected IConfigModelForBoarding               configDetails;
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleClassMappingModel.class)
  public void setClassMappings(List<IConfigRuleClassMappingModel> classMappings)
  {
    this.classMappings = classMappings;
  }
  
  @Override
  public List<IConfigRuleClassMappingModel> getClassMappings()
  {
    
    return classMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTaxonomyMappingModel.class)
  public void setTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> taxonomyMappings)
  {
    this.taxonomyMappings = taxonomyMappings;
  }
  
  @Override
  public List<IConfigRuleTaxonomyMappingModel> getTaxonomyMappings()
  {
    
    return taxonomyMappings;
  }
  
  @Override
  public IConfigModelForBoarding getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ConfigModelForBoarding.class)
  public void setConfigDetails(IConfigModelForBoarding configDetails)
  {
    this.configDetails = configDetails;
  }
  
}
