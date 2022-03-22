package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.datarule.IMandatoryProperty;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllMandatoryAttributeModel extends IModel {
  
  public List<? extends IMandatoryProperty> getProjectKlassAttributes();
  
  public void setProjectKlassAttributes(List<? extends IMandatoryProperty> attributes);
  
  public List<? extends IMandatoryProperty> getTaskKlassAttributes();
  
  public void setTaskKlassAttributes(List<? extends IMandatoryProperty> attributes);
  
  public List<? extends IMandatoryProperty> getAssetKlassAttributes();
  
  public void setAssetKlassAttributes(List<? extends IMandatoryProperty> attributes);
  
  public List<? extends IMandatoryProperty> getTargetKlassAttributes();
  
  public void setTargetKlassAttributes(List<? extends IMandatoryProperty> attributes);
}
