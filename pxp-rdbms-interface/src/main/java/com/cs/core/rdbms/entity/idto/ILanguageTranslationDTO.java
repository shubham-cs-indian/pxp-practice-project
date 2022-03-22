package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface ILanguageTranslationDTO extends IRootDTO, ISimpleDTO {

  /**
   * @return the locale Id 
   */
  public String getLocaleId();

  /**
   * @param localeId
   */
  public void setLocaleId(String localeId);

  /**
   * @return the baseEntityIID of the entity
   */
  public long getBaseEntityIID();

  /**
   * @param baseEntityIID
   */
  public void setBaseEntityIID(long baseEntityIID);
  
}
