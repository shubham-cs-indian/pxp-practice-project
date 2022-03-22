package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.templating.AbstractGetKlassInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTaskInstanceResponseModel extends AbstractGetKlassInstanceModel
    implements IGetTaskInstanceResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected Long                                count;
  protected List<ITaskInstanceInformationModel> taskInstanceList;
  protected List<IConfigEntityInformationModel> referencedVariants;
  protected Map<String, IIdAndTypeModel>        referencedElementsMapping;
  protected Map<String, String>                 referencedElements;
  
  @Override
  public Map<String, String> getReferencedElements()
  {
    if(referencedElements == null) {
      referencedElements = new HashMap<>();
    }
    return referencedElements;
  }
  
  @Override
  public void setReferencedElements(Map<String, String> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IIdAndTypeModel> getReferencedElementsMapping()
  {
    if(this.referencedElementsMapping == null) {
      this.referencedElementsMapping = new HashMap<String, IIdAndTypeModel>();
    }
    return referencedElementsMapping;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setReferencedElementsMapping(Map<String, IIdAndTypeModel> referencedElementsMapping)
  {
    this.referencedElementsMapping = referencedElementsMapping;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getReferencedVariants()
  {
    return referencedVariants;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedVariants(List<IConfigEntityInformationModel> referencedVariants)
  {
    this.referencedVariants = referencedVariants;
  }
  
  @Override
  public List<ITaskInstanceInformationModel> getTaskInstanceList()
  {
    if(this.taskInstanceList == null) {
      this.taskInstanceList = new ArrayList<>();
    }
    return taskInstanceList;
  }
  
  @JsonDeserialize(contentAs = TaskInstanceInformationModel.class)
  @Override
  public void setTaskInstanceList(List<ITaskInstanceInformationModel> taskInstanceList)
  {
    this.taskInstanceList = taskInstanceList;
  }
}
