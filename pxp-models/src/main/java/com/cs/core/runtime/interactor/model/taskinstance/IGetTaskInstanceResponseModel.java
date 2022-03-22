package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

import java.util.List;
import java.util.Map;

public interface IGetTaskInstanceResponseModel extends IGetKlassInstanceModel {
  
  public static final String TASK_INSTANCE_LIST           = "taskInstanceList";
  public static final String REFERENCED_VARIANTS          = "referencedVariants";
  public static final String REFERENCED_ELEMENTS_MAPPINGS = "referencedElementsMapping";
  public static final String REFERENCED_ELEMENTS          = "referencedElements";
  
  public List<ITaskInstanceInformationModel> getTaskInstanceList();
  
  public void setTaskInstanceList(List<ITaskInstanceInformationModel> taskInstanceList);
  
  public List<IConfigEntityInformationModel> getReferencedVariants();
  
  public void setReferencedVariants(List<IConfigEntityInformationModel> referencedVariants);
  
  public Map<String, IIdAndTypeModel> getReferencedElementsMapping();
  
  public void setReferencedElementsMapping(Map<String, IIdAndTypeModel> referencedElementsMapping);
  
  public Map<String, String> getReferencedElements();
  
  public void setReferencedElements(Map<String, String> referencedElements);
}
