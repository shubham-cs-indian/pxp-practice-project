package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IBaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IElasticBulkUpdateDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import org.json.simple.JSONObject;

import java.util.*;

public class ElasticBulkUpdateDTO extends InitializeBGProcessDTO implements IElasticBulkUpdateDTO {

  private final String TRANSACTION_ID = "transactionId";
  private       String transactionId;

  @Override
  public String getTransactionId() {
    return transactionId;
  }

  @Override
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(TRANSACTION_ID, transactionId));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    transactionId = json.getString(TRANSACTION_ID);
  }
}
