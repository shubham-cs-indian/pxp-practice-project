package com.cs.core.rdbms.entity.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.ILanguageTranslationDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;

public class LanguageTranslationDTO extends RDBMSRootDTO implements ILanguageTranslationDTO{

  private static final long serialVersionUID = 1L;
  
  protected String             localeId;
  protected long               baseEntityIID;
  
  public LanguageTranslationDTO(String localeId, long baseEntityIID)
  {
    super();
    this.localeId = localeId;
    this.baseEntityIID = baseEntityIID;
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Translation);
    cse.setCode(localeId);
    cse.setIID(baseEntityIID);
    cse.setSpecification(Keyword.$locale, localeId);
    return cse;
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    localeId = ((CSEObject) cse).getSpecification(Keyword.$locale);
  }
  
  @Override
  public String getLocaleId()
  {
    return localeId;
  }

  @Override
  public void setLocaleId(String localeId)
  {
    this.localeId = localeId;
  }

  @Override
  public long getBaseEntityIID()
  {
    return baseEntityIID;
  }

  @Override
  public void setBaseEntityIID(long baseEntityIID)
  {
    this.baseEntityIID = baseEntityIID;
  }
  
}
