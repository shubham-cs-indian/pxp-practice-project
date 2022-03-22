package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface ILanguageInheritanceDTO extends IInitializeBGProcessDTO{
  
  public static final String BASE_ENTITY_IID = "baseEntityIID";
  public static final String LOCALE_IIDs = "localeIIDs";
  public static final String DEPENDENT_PROPERTY_IIDS = "dependentPropertyIIDs";
  
  
  public void setBaseEntityIID(Long baseEntityIID);
  public Long getBaseEntityIID();
  
  public void setLocaleIIDs(List<String> localeIIDs);
  public List<String> getLocaleIIDs();
  
  public void setDependentPropertyIIDs(List<Long> dependentPropertyIIDs);
  public List<Long> getDependentPropertyIIDs();
}
