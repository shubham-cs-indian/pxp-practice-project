package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.relationship.ReferencedRelationshipModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.model.relationship.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForSaveRelationshipInstancesResponseModel
    implements IGetConfigDetailsForSaveRelationshipInstancesResponseModel {
  
  private static final long                                     serialVersionUID = 1L;
  protected Map<String, IReferencedSectionElementModel>         referencedElements;
  protected Map<String, ITag>                                   referencedTags;
  protected Map<String, IGetReferencedNatureRelationshipModel>  referencedNatureRelationships;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Map<String, IReferencedRelationshipModel>           referencedRelationships;
  protected IReferencedContextModel                             referencedVariantContexts;
  protected List<String>                                        side2LinkedVariantKrIds;
  protected List<Long>                                        linkedVariantPropertyIids;
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<String>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipModel.class)
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }

  @Override
  public List<Long> getLinkedVariantPropertyIids()
  {
    if (linkedVariantPropertyIids == null) {
      linkedVariantPropertyIids = new ArrayList<Long>();
    }
    return linkedVariantPropertyIids;
  }

  @Override
  public void setLinkedVariantPropertyIids(List<Long> linkedVariantPropertyIids)
  {
    this.linkedVariantPropertyIids= linkedVariantPropertyIids;
  }
}
