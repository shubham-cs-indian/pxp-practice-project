package com.cs.core.elastic.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

import java.util.Map;
import java.util.Set;

public interface IEntityElasticUpdateInfoDTO {

  Set<IPropertyRecordDTO> getPropertiesToDelete();

  void setPropertiesToDelete(Set<IPropertyRecordDTO> propertiesToDelete);

  long getEntityIID();
  void setEntityIID(long entityIID);

  BaseType getBaseType();
  void setBaseType(BaseType baseType);

  Set<IPropertyRecordDTO> getPropertiesToModify();
  void setPropertiesToModify(Set<IPropertyRecordDTO> propertiesToModify);
}
