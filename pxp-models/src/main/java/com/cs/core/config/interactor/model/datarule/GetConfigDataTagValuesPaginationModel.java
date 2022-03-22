package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.model.configdetails.GetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataTagValuesPaginationModel;

import java.util.List;

public class GetConfigDataTagValuesPaginationModel extends GetConfigDataEntityPaginationModel
    implements IGetConfigDataTagValuesPaginationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          tagGroupId;
  protected String          elementId;
  protected String          klassInstanceId;
  protected String          baseType;
  protected List<String>    klassTypes;
  protected List<String>    selectedTaxonomyIds;
  protected String          taxonomyId;
  protected String          contextId;
  
  @Override
  public String getTagGroupId()
  {
    return tagGroupId;
  }
  
  @Override
  public void setTagGroupId(String tagGroupId)
  {
    this.tagGroupId = tagGroupId;
  }
  
  @Override
  public String getElementId()
  {
    return elementId;
  }
  
  @Override
  public void setElementId(String elementId)
  {
    this.elementId = elementId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String moduleId)
  {
    this.baseType = moduleId;
  }
  
  @Override
  public List<String> getKlassTypes()
  {
    return klassTypes;
  }
  
  @Override
  public void setKlassTypes(List<String> klassTypes)
  {
    this.klassTypes = klassTypes;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getTaxonomyId()
  {
    return taxonomyId;
  }
  
  @Override
  public void setTaxonomyId(String taxonomyId)
  {
    this.taxonomyId = taxonomyId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }

  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
}
