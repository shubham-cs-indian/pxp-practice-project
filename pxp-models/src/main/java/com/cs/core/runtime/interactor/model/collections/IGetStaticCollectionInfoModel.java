package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetStaticCollectionInfoModel extends IModel {
  
  public static final String SECTIONS                       = "sections";
  public static final String CHILDREN_IDS                   = "childrenIds";
  public static final String DEFAULT_VALUES_DIFF            = "defaultValuesDiff";
  public static final String DELETED_PROPERTIES_FROM_SOURCE = "deletedPropertiesFromSource";
  public static final String IS_COLLECTION_MOVABLE          = "isCollectionMovable";
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public List<String> getChildrenIds();
  
  public void setChildrenIds(List<String> childrenIds);
  
  /**
   * Map key -> entityId , Value -> klassIds (usecase : remove
   * propertyCollection From klass or remove property from propertyCollection)
   */
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public Boolean getIsCollectionMovable();
  
  public void setIsCollectionMovable(Boolean isCollectionMovable);
}
