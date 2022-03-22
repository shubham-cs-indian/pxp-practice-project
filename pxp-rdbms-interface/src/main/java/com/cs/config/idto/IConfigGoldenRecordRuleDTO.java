package com.cs.config.idto;

import java.util.List;

/**
 * Golden record rule DTO from the configuration realm
 *
 * @author janak
 */
public interface IConfigGoldenRecordRuleDTO extends IConfigJSONDTO {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getAttributes();
  
  public void setAttributes(List<String> attributes);
  
  public List<String> getTags();
  
  public void setTags(List<String> tags);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getOrganizations();
  
  public void setOrganizations(List<String> organizations);
  
  public IConfigMergeEffectDTO getMergeEffect();
  
  public void setMergeEffect(IConfigMergeEffectDTO mergeEffect);
  
  public Boolean getIsAutoCreate();
  
  public void setIsAutoCreate(Boolean isAutoCreate);
  
  public void setCode(String code);
}
