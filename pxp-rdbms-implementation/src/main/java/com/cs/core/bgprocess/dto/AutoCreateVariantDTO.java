package com.cs.core.bgprocess.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.json.simple.JSONObject;

import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;

public class AutoCreateVariantDTO extends SimpleDTO implements IAutoCreateVariantDTO {
  
  /**
   * 
   */
  private static final long   serialVersionUID = 1L;
  public static final String  Entity           = "entity";
  public static final String  CONFIG_DATA      = "configInfo";
  public static final String  TRANSACTION      = "transaction";
  
  private IBaseEntityDTO      entity           = new BaseEntityDTO();
  private Map<String, Object> configInfo       = new HashMap<>();
  private TransactionData     transactionData  = new TransactionData();
  
  @Override
  @SuppressWarnings("unchecked")
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    try {
      String baseEntity = json.getString(Entity);
      entity.fromPXON(baseEntity);
      JSONContent configData = json.getJSONContent(CONFIG_DATA);
      this.configInfo.putAll(ObjectMapperUtil.readValue(configData.toString(), Map.class));
      JSONContent transaction = json.getJSONContent(TRANSACTION);
      transactionData = ObjectMapperUtil.readValue(transaction.toString(), TransactionData.class);
    }
    catch (Exception e) {
      throw new CSFormatException(e.getMessage(), e);
    }
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    JSONContent jsonContent = new JSONContent(ObjectMapperUtil.convertValue(configInfo, JSONObject.class));
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(Entity, entity.toPXON()),
        configInfo.size() > 0 ? JSONBuilder.newJSONField(CONFIG_DATA, jsonContent.toString()) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(TRANSACTION, transactionData.toString()));
  }
  
  @Override
  public IBaseEntityDTO getBaseEntity()
  {
    return entity;
  }
  
  @Override
  public void setBaseEntity(IBaseEntityDTO baseEntity)
  {
    entity = baseEntity;
  }
  
  @Override
  public Map<String, Object> getConfigData()
  {
    return configInfo;
  }
  
  @Override
  public void setConfigData(Map<String, Object> configModel)
  {
    configInfo = configModel;
  }
  
  @Override
  public JSONContent toJSONContent() throws CSFormatException
  {
    JSONContent infoMap = new JSONContent();
    infoMap.setField(Entity, entity.toPXON());
    infoMap.setField(CONFIG_DATA, ObjectMapperUtil.convertValue(configInfo, JSONObject.class));
    infoMap.setField(TRANSACTION, ObjectMapperUtil.convertValue(transactionData, JSONObject.class));
    return infoMap;
  }
  
  /**
   * set the transaction data by copying current transaction info and set the
   * level of the transaction
   * 
   * @param transactionData
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public void setTransaction(TransactionData transactionData) throws IllegalAccessException, InvocationTargetException
  {
    BeanUtils.copyProperties(this.transactionData, transactionData);
    this.transactionData.setLevel(this.transactionData.getLevel() + 1);
  }
  
  /**
   * 
   * @return the TransactionData of the current thread
   */
  public TransactionData getTransaction()
  {
    return transactionData;
  }
}
