package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForContentGridViewModel extends IModel {
  
  public static final String INSTNACE_CONFIG_DETAILS  = "instanceConfigDetails";
  public static final String REFERENCED_ATTRIBUTES    = "referencedAttributes";
  public static final String REFERENCED_TAGS          = "referencedTags";
  public static final String ALLOWED_PROPERTY_IDS     = "allowedPropertyIds";
  public static final String PROPERTY_SEQUENDCE_LISTS = "gridPropertySequenceList";
  
  // klassInstanceId : entityId : referencedElementMap
  public Map<String, IGridInstanceConfigDetailsModel> getInstanceConfigDetails();
  
  public void setInstanceConfigDetails(
      Map<String, IGridInstanceConfigDetailsModel> instanceConfigDetails);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<String> getAllowedPropertyIds();
  
  public void setAllowedPropertyIds(List<String> allowedPropertyIds);
  
  public List<String> getGridPropertySequenceList();
  
  public void setGridPropertySequenceList(List<String> gridPropertySequenceList);
}
