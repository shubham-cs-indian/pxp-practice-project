package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IMdmPropertiesModel extends IModel {
  
  public static final String ATTRIBUTE_INSTANCES = "attributeInstances";
  public static final String TAG_INSTANCES       = "tagInstances";
  
  public List<IAttributeInstance> getAttributeInstances();
  
  public void setAttributeInstances(List<IAttributeInstance> attributeInstances);
  
  public List<ITagInstance> getTagInstances();
  
  public void setTagInstances(List<ITagInstance> tagInstances);
}
