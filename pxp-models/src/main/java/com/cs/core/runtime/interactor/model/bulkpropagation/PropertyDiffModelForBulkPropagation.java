package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IContextKlassSavePropertiesToInheritModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.model.context.ContextualValueInheritancePropagationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class PropertyDiffModelForBulkPropagation implements IPropertyDiffModelForBulkPropagation {
  
  private static final long                           serialVersionUID = 1L;
  protected List<IDefaultValueChangeModel>            defaultValueDiffList;
  protected List<IIdAndBaseType>                      instanceInfoList;
  protected IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel;
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValueDiffList()
  {
    if (defaultValueDiffList == null) {
      defaultValueDiffList = new ArrayList<>();
    }
    return defaultValueDiffList;
  }
  
  @Override
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  public void setDefaultValueDiffList(List<IDefaultValueChangeModel> defaultValueDiffList)
  {
    this.defaultValueDiffList = defaultValueDiffList;
  }
  
  @Override
  public List<IIdAndBaseType> getInstanceInfoList()
  {
    if (instanceInfoList == null) {
      instanceInfoList = new ArrayList<>();
    }
    return instanceInfoList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setInstanceInfoList(List<IIdAndBaseType> instanceInfoList)
  {
    this.instanceInfoList = instanceInfoList;
  }
  
  @Override
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInheritModel()
  {
    return contextKlassSavePropertiesToInheritModel;
  }
  
  @JsonDeserialize(as = ContextualValueInheritancePropagationModel.class)
  @Override
  public void setContextKlassSavePropertiesToInheritModel(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel)
  {
    this.contextKlassSavePropertiesToInheritModel = contextKlassSavePropertiesToInheritModel;
  }
}
