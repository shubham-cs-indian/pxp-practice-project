package com.cs.core.config.interactor.model.grideditpropertylist;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

public interface IGetGridEditPropertyListSuccessModel extends IConfigResponseWithAuditLogModel {
  
  public static final String PROPERTY_LIST          = "propertyList";
  public static final String PROPERTY_SEQUENCE_LIST = "propertySequenceList";
  public static final String PROPERTY_LIST_TOTAL_COUNT = "propertyListTotalCount";
  
  public List<IIdLabelTypeModel> getPropertyList();
  
  public void setPropertyList(List<IIdLabelTypeModel> propertyList);
  
  public List<IIdLabelTypeModel> getPropertySequenceList();
  
  public void setPropertySequenceList(List<IIdLabelTypeModel> propertySequenceList);
  
  public Long getPropertyListTotalCount();
  
  public void setPropertyListTotalCount(Long propertyListTotalCount);
}
