package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Date;
import java.util.List;

public interface IUnitDiModel extends IModel {
  
  public static final String TIMESTAMP        = "timestamp";
  public static final String UNIT_COLLECTIONS = "unitInfo";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
  
  public List<IUnitCollections> getUnitCollections();
  
  public void setUnitCollections(List<IUnitCollections> unitCollections);
}
