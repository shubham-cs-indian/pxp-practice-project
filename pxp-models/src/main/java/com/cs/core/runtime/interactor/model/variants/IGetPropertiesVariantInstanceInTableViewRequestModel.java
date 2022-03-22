package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.filter.IBasicFilterRequestModel;

import java.util.List;

public interface IGetPropertiesVariantInstanceInTableViewRequestModel
    extends IBasicFilterRequestModel {
  
  public static final String CONTEXT_ID        = "contextId";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String PARENT_ID         = "parentId";
  public static final String ATTRIBUTE_ID      = "attributeId";
  public static final String TEMPLATE_ID       = "templateId";
  public static final String COLUMN_IDS        = "columnIds";
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String contentId);
  
  public IVariantFilterRequestModel getFilterInfo();
  
  public void setFilterInfo(IVariantFilterRequestModel filterInfo);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getColumnIds();
  
  public void setColumnIds(List<String> columnIds);
}
