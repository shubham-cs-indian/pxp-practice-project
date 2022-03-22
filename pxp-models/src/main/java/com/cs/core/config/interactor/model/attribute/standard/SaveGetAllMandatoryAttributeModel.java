package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.datarule.IMandatoryProperty;

import java.util.List;

public class SaveGetAllMandatoryAttributeModel implements IGetAllMandatoryAttributeModel {
  
  protected List<? extends IMandatoryProperty> projectKlassAttributes;
  protected List<? extends IMandatoryProperty> taskKlassAttributes;
  protected List<? extends IMandatoryProperty> assetKlassAttributes;
  protected List<? extends IMandatoryProperty> targetKlassAttributes;
  
  @Override
  public List<? extends IMandatoryProperty> getProjectKlassAttributes()
  {
    return projectKlassAttributes;
  }
  
  @Override
  public void setProjectKlassAttributes(List<? extends IMandatoryProperty> projectKlassAttributes)
  {
    this.projectKlassAttributes = projectKlassAttributes;
  }
  
  @Override
  public List<? extends IMandatoryProperty> getTaskKlassAttributes()
  {
    return taskKlassAttributes;
  }
  
  @Override
  public void setTaskKlassAttributes(List<? extends IMandatoryProperty> taskKlassAttributes)
  {
    this.taskKlassAttributes = taskKlassAttributes;
  }
  
  @Override
  public List<? extends IMandatoryProperty> getAssetKlassAttributes()
  {
    return assetKlassAttributes;
  }
  
  @Override
  public void setAssetKlassAttributes(List<? extends IMandatoryProperty> attributes)
  {
    this.assetKlassAttributes = attributes;
  }
  
  @Override
  public List<? extends IMandatoryProperty> getTargetKlassAttributes()
  {
    return targetKlassAttributes;
  }
  
  @Override
  public void setTargetKlassAttributes(List<? extends IMandatoryProperty> targetKlassAttributes)
  {
    this.targetKlassAttributes = targetKlassAttributes;
  }
}
