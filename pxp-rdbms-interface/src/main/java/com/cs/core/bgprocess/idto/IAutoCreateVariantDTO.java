package com.cs.core.bgprocess.idto;

import java.util.Map;

import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAutoCreateVariantDTO extends ISimpleDTO {
  
  public static final String AUTO_CREATE_VARIANT_SERVICE = "AUTO_CREATE_VARIANT";
  public static final String CREATE_VARIANT_SERVICE = "CREATE_VARIANT";
  public static final String AUTO_CREATE_TIV_SERVICE = "AUTO_CREATE_TIV";
  
  public IBaseEntityDTO getBaseEntity();
  
  /**
   * 
   * @param baseEntity set the current baseEntity
   */
  public void setBaseEntity(IBaseEntityDTO baseEntity);
  
  public Map<String, Object> getConfigData();
  
  /**
   * 
   * @param configModel config data model
   */
  public void setConfigData(Map<String, Object> configModel);
  
  public IJSONContent toJSONContent() throws CSFormatException;
}
