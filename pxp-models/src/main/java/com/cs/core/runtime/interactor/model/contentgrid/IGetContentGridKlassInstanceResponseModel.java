package com.cs.core.runtime.interactor.model.contentgrid;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridContentInstanceModel;

import java.util.List;
import java.util.Map;

public interface IGetContentGridKlassInstanceResponseModel extends IModel {
  
  public static final String KLASS_INSTANCES             = "klassInstances";
  public static final String REFERENCED_ATTRIBUTES       = "referencedAttributes";
  public static final String REFERENCED_TAGS             = "referencedTags";
  public static final String PROPERTY_LIST               = "propertyList";
  public static final String GRID_PROPERTY_SEQUENCE_LIST = "gridPropertySequenceList";
  
  public List<IGridContentInstanceModel> getKlassInstances();
  
  public void setKlassInstances(List<IGridContentInstanceModel> klassInstance);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<String> getPropertyList();
  
  public void setPropertyList(List<String> propertyList);
  
  public List<String> getGridPropertySequenceList();
  
  public void setGridPropertySequenceList(List<String> gridPropertySequenceList);
}
