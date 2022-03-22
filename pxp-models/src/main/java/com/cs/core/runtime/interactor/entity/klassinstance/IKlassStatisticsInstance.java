package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import java.util.List;

public interface IKlassStatisticsInstance extends IRuntimeEntity {
  
  public static final String NAME                       = "name";
  public static final String BASETYPE                   = "baseType";
  public static final String KLASS_INSTANCE_ID          = "klassInstanceId";
  public static final String PARENT_VARIANT_INSTANCE_ID = "parentVariantInstanceId";
  public static final String DATA_GOVERNANCE            = "dataGovernance";
  
  public String getName();
  
  public void setName(String name);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getParentVariantInstanceId();
  
  public void setParentVariantInstanceId(String parentVariantId);
  
  public List<IStatistics> getDataGovernance();
  
  public void setDataGovernance(List<IStatistics> dataGovernance);
}
