package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IReferencedPropertyCollectionModel extends IConfigMasterEntity, IModel {
  
  public static final String IS_COLLAPSED           = "isCollapsed";
  public static final String ELEMENTS               = "elements";
  public static final String ROWS                   = "rows";
  public static final String COLUMNS                = "columns";
  public static final String SEQUENCE               = "sequence";
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  public static final String IS_HIDDEN              = "isHidden";
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
  
  public List<IReferencedPropertyCollectionElementModel> getElements();
  
  public void setElements(List<IReferencedPropertyCollectionElementModel> elements);
  
  public Integer getRows();
  
  public void setRows(Integer rows);
  
  public Integer getColumns();
  
  public void setColumns(Integer columns);
  
  public Integer getSequence();
  
  public void setSequence(Integer sequence);
  
  public String getPropertyCollectionId();
  
  public void setPropertyCollectionId(String properyCollectionId);
  
  public Boolean getIsHidden();
  
  public void setIsHidden(Boolean isHidden);
}
