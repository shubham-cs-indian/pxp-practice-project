package com.cs.core.runtime.interactor.model.version;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.instance.AbstractGetInstanceRequestStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceVersionsComparisonModel extends AbstractGetInstanceRequestStrategyModel
    implements IKlassInstanceVersionsComparisonModel {
  
  private static final long                             serialVersionUID = 1L;
  protected List<String>                                versionIds;
  protected List<String>                                referencedNatureRelationshipsIds;
  protected List<String>                                referencedRelationshipsIds;
  protected List<String>                                referencedLifeCycleStatusTags;
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  
  @Override
  public List<String> getVersionIds()
  {
    if (versionIds == null) {
      versionIds = new ArrayList<>();
    }
    return versionIds;
  }
  
  @Override
  public void setVersionIds(List<String> versionIds)
  {
    this.versionIds = versionIds;
  }
  
  @Override
  public List<String> getReferencedNatureRelationshipsIds()
  {
    if (referencedNatureRelationshipsIds == null) {
      referencedNatureRelationshipsIds = new ArrayList<>();
    }
    return referencedNatureRelationshipsIds;
  }
  
  @Override
  public void setReferencedNatureRelationshipsIds(List<String> referencedNatureRelationshipsIds)
  {
    this.referencedNatureRelationshipsIds = referencedNatureRelationshipsIds;
  }
  
  @Override
  public List<String> getReferencedRelationshipsIds()
  {
    if (referencedRelationshipsIds == null) {
      referencedRelationshipsIds = new ArrayList<>();
    }
    return referencedRelationshipsIds;
  }
  
  @Override
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds)
  {
    this.referencedRelationshipsIds = referencedRelationshipsIds;
  }
  
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    if (referencedLifeCycleStatusTags == null) {
      referencedLifeCycleStatusTags = new ArrayList<>();
    }
    return referencedLifeCycleStatusTags;
  }
  
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    this.referencedLifeCycleStatusTags = referencedLifeCycleStatusTags;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    if (referencedElements == null) {
      referencedElements = new HashMap<>();
    }
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
}
