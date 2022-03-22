package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "attribute",
        value = ReferencedSectionAttributeForSwitchTypeModel.class),
    @JsonSubTypes.Type(name = "tag",
        value = ReferencedSectionTagForSwitchTypeModel.class),
    @JsonSubTypes.Type(name = "relationship",
        value = ReferencedSectionRelationshipForSwitchTypeModel.class),
    @JsonSubTypes.Type(name = "role",
        value = ReferencedSectionRoleForSwitchTypeModel.class),
    @JsonSubTypes.Type(name = "taxonomy",
        value = ReferencedSectionTaxonomyForSwitchTypeModel.class)})
public abstract class AbstractReferencedSectionElementForSwitchTypeModel
    extends AbstractReferencedSectionElementModel implements IReferencedSectionForSwitchTypeModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceId;
  protected String          sourceType;
  
  @Override
  public String getSourceId()
  {
    return sourceId;
  }
  
  @Override
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  @Override
  public String getSourceType()
  {
    return sourceType;
  }
  
  @Override
  public void setSourceType(String sourceType)
  {
    this.sourceType = sourceType;
  }
}
