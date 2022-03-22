package com.cs.config.idto;

import java.util.List;

import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author roshani.waghmare
 */
public interface IConfigDataRuleDTO extends IConfigJSONDTO {
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IConfigDataRuleIntermediateEntitysDTO> getAttributes();
  
  public List<String> getTypes();
  
  public List<IConfigDataRuleTagsDTO> getTags();
  
  public List<IJSONContent> getRuleViolations();
  
  public List<IConfigNormalizationDTO> getNormalizations();
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public List<String> getTaxonomies();
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getOrganizations();
  
  public List<String> getPhysicalCatalogIds();
  
  public Boolean getIsLanguageDependent();
  
  public void setIsLanguageDependent(Boolean isLanguageDependent);
  
  public List<String> getLanguages();
  
  /**
   * 
   * @param types set klass codes through setter not through get
   */
  public void setTypes(List<String> types);
  
  /**
   * 
   * @param organizations set organization code through only setter
   */
  public void setOrganizations(List<String> organizations);
  
  /**
   * 
   * @param ruleViolations set violation json
   */
  public void setRuleVioloation(List<IJSONContent> ruleViolations);
  
  /**
   * 
   * @param taxonomyCodes set taxonomy codes
   */
  public void setTaxonomyCodes(List<String> taxonomyCodes);
  
  /**
   * 
   * @param physicalCatalogIds set physicalCatalog codes
   */
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  /**
   * 
   * @param langugesCodes set language code
   */
  public void setLanguages(List<String> languagesCodes);
  
}
