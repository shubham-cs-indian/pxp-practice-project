package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import java.util.List;

public interface IDuplicateTypeAndInstanceIds extends IEntity {
  
  public static final String TYPE_ID                      = "typeId";
  public static final String DUPLICATE_KLASS_INSTANCE_IDS = "duplicateKlassInstanceIds";
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public List<String> getDuplicateKlassInstanceIds();
  
  public void setDuplicateKlassInstanceIds(List<String> duplicateKlassInstanceIds);
}
