package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;

public interface IAbstractMappingModel extends IConfigModel, IMapping {
  
  public static final String CLASS_MAPPINGS    = "classMappings";
  public static final String TAXONOMY_MAPPINGS = "taxonomyMappings";
  public static final String CONFIG_DETAILS    = "configDetails";
  
  public void setClassMappings(List<IConfigRuleClassMappingModel> classMappings);
  
  public List<IConfigRuleClassMappingModel> getClassMappings();
  
  public void setTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> taxonomyMappings);
  
  public List<IConfigRuleTaxonomyMappingModel> getTaxonomyMappings();
  
  public IConfigModelForBoarding getConfigDetails();
  
  public void setConfigDetails(IConfigModelForBoarding configDetails);
}
