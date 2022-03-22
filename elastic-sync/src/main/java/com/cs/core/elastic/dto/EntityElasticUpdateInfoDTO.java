package com.cs.core.elastic.dto;

import com.cs.core.elastic.idto.IEntityElasticUpdateInfoDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;

import java.util.HashSet;
import java.util.Set;

public class EntityElasticUpdateInfoDTO implements IEntityElasticUpdateInfoDTO {

  private long                    entityIID;
  private BaseType                baseType;
  private Set<IPropertyRecordDTO> propertiesToModify = new HashSet<>();
  private Set<IPropertyRecordDTO> propertiesToDelete = new HashSet<>();

  @Override
  public Set<IPropertyRecordDTO> getPropertiesToDelete()
  {
    return propertiesToDelete;
  }

  @Override
  public void setPropertiesToDelete(Set<IPropertyRecordDTO> propertiesToDelete)
  {
    this.propertiesToDelete = propertiesToDelete;
  }

  @Override
  public long getEntityIID() {
    return entityIID;
  }

  @Override
  public void setEntityIID(long entityIID) {
    this.entityIID = entityIID;
  }

  @Override
  public BaseType getBaseType() {
    return baseType;
  }

  @Override
  public void setBaseType(BaseType baseType) {
    this.baseType = baseType;
  }

  @Override
  public Set<IPropertyRecordDTO> getPropertiesToModify() {
    return propertiesToModify;
  }

  @Override
  public void setPropertiesToModify(Set<IPropertyRecordDTO> propertiesToModify) {
    this.propertiesToModify = propertiesToModify;
  }
}
