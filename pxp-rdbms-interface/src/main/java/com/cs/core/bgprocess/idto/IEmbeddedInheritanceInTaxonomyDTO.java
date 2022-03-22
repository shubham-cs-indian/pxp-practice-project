package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IEmbeddedInheritanceInTaxonomyDTO extends ISimpleDTO {
  
  public String getId();
  public void setId(String id);
  
  public String getCode();
  public void setCode(String code);
  
  public String getLabel();
  public void setLabel(String label);
  
  public List<String> getAddedContextKlasses();
  public void setAddedContextKlasses(List<String> addedContextKlasses);
}
