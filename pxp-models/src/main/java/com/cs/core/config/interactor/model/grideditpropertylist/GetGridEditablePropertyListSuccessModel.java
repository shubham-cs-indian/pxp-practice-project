package com.cs.core.config.interactor.model.grideditpropertylist;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGridEditablePropertyListSuccessModel extends ConfigResponseWithAuditLogModel
    implements IGetGridEditPropertyListSuccessModel {
  
  private static final long serialVersionUID = 1L;
  List<IIdLabelTypeModel>   propertyList;
  List<IIdLabelTypeModel>   propertySequenceList;
  protected Long            propertyListTotalCount;
  
  @Override
  public List<IIdLabelTypeModel> getPropertyList()
  {
    if (propertyList == null) {
      propertyList = new ArrayList<IIdLabelTypeModel>();
    }
    return propertyList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setPropertyList(List<IIdLabelTypeModel> propertyList)
  {
    this.propertyList = propertyList;
  }
  
  
  @Override
  public Long getPropertyListTotalCount()
  {
    return propertyListTotalCount;
  }
  
  @Override
  public void setPropertyListTotalCount(Long propertyListTotalCount)
  {
    this.propertyListTotalCount = propertyListTotalCount;
  }
  
  @Override
  public List<IIdLabelTypeModel> getPropertySequenceList()
  {
    if (propertySequenceList == null) {
      propertySequenceList = new ArrayList<IIdLabelTypeModel>();
    }
    return propertySequenceList;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setPropertySequenceList(List<IIdLabelTypeModel> propertySequenceList)
  {
    this.propertySequenceList = propertySequenceList;
  }
}
