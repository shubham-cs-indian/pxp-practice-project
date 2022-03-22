package com.cs.core.bgprocess.idto;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

import java.util.List;
import java.util.Set;

public interface IElasticBulkUpdateDTO extends IInitializeBGProcessDTO {

  String getTransactionId();

  void setTransactionId(String transactionId);
}
