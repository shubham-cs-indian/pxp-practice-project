package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// Note: update IReferencedPropertyCollectionModel when new properties are added
@JsonIgnoreProperties(ignoreUnknown = true)
public interface ISection extends IConfigMasterEntity {
  
  public static final String IS_COLLAPSED           = "isCollapsed";
  public static final String ELEMENTS               = "elements";
  public static final String ROWS                   = "rows";
  public static final String COLUMNS                = "columns";
  public static final String SEQUENCE               = "sequence";
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  public static final String IS_HIDDEN              = "isHidden";
  public static final String IS_SKIPPED             = "isSkipped";
  public static final String IS_INHERITED           = "isInherited";
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
  
  public List<ISectionElement> getElements();
  
  public void setElements(List<ISectionElement> elements);
  
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
  
  public Boolean getIsSkipped();
  
  public void setIsSkipped(Boolean isSkipped);
  
  public Boolean getIsInherited();
  
  public void setIsInherited(Boolean isInherited);
}
