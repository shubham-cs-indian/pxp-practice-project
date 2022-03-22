package com.cs.config.idto;

public interface IConfigNatureRelationshipDTO extends IConfigRelationshipDTO {
  
  public String getTaxonomyInheritanceSetting();
  
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting);
}
