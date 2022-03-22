package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Date;
import java.util.List;

public interface INomenclatureDiModel extends IModel {
  
  public static final String TIMESTAMP         = "timestamp";
  public static final String ROOTNOMENCLATURES = "rootnomenclatures";
  
  public Date getTimestamp();
  
  public void setTimestamp(Date timestamp);
  
  public List<IRootNomenclatureModel> getRootnomenclatures();
  
  public void setRootnomenclatures(List<IRootNomenclatureModel> rootnomenclatures);
}
