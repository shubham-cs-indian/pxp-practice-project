package com.cs.core.bgprocess.idto;

import java.util.Set;

public interface IDataRuleDeleteDTO extends IInitializeBGProcessDTO {
  
  public void setRuleCodes(Set<String> ruleCodes);
  
  public Set<String> getRuleCodes(); 
  
  public void setKlassIds(Set<String> klassIds);
  
  public Set<String> getKlassIds();
  
  public void setTaxonomyIds(Set<String> taxonomyIds);
  
  public Set<String> getTaxonomyIds();
  
  public void setCatalogIds(Set<String> catalogIds);
  
  public Set<String> getCatalogIds();

}
