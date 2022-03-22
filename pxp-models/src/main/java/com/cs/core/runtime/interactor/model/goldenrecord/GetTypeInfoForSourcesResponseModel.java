package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.interactor.model.klass.TypesListModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTypeInfoForSourcesResponseModel implements IGetTypeInfoForSourcesResponseModel {
  
  private static final long              serialVersionUID = 1L;
  protected Map<String, ITypesListModel> sourceIdTypeInfoMap;
  protected List<String>                 sourceIds;
  protected IConfigDetailsForTypeInfoModel 	     configDetails;
   
  @Override
  public Map<String, ITypesListModel> getSourceIdTypeInfoMap()
  {
    return sourceIdTypeInfoMap;
  }
  
  @JsonDeserialize(contentAs = TypesListModel.class)
  @Override
  public void setSourceIdTypeInfoMap(Map<String, ITypesListModel> sourceIdTypeInfoMap)
  {
    this.sourceIdTypeInfoMap = sourceIdTypeInfoMap;
  }
  
  @Override
  public List<String> getSourceIds()
  {
    return sourceIds;
  }
  
  @Override
  public void setSourceIds(List<String> sourceIds)
  {
    this.sourceIds = sourceIds;
  }

@Override
public void setConfigDetails(IConfigDetailsForTypeInfoModel configDetails) {
	this.configDetails = configDetails;
}

@Override
public IConfigDetailsForTypeInfoModel getConfigDetails() {
	return configDetails;
}
}
