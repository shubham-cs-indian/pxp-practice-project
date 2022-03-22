package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IContextKlassSavePropertiesToInheritModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IPropertyDiffModelForBulkPropagation extends IModel {
  
  public static final String DEFAULT_VALUE_DIFF_LIST                        = "defaultValueDiffList";
  public static final String INSTANCE_INFO_LIST                             = "instanceInfoList";
  public static final String CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT_MODEL = "contextKlassSavePropertiesToInheritModel";
  
  public List<IDefaultValueChangeModel> getDefaultValueDiffList();
  
  public void setDefaultValueDiffList(List<IDefaultValueChangeModel> defaultValueDiffList);
  
  public List<IIdAndBaseType> getInstanceInfoList();
  
  public void setInstanceInfoList(List<IIdAndBaseType> instanceInfoList);
  
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInheritModel();
  
  public void setContextKlassSavePropertiesToInheritModel(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInheritModel);
}
