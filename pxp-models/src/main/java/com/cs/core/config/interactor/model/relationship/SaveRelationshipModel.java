package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.SaveRelationshipSide;
import com.cs.core.config.interactor.model.configdetails.DeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.configdetails.ModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.AbstractModifiedSectionElementModel;
import com.cs.core.config.interactor.model.klass.AddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = SaveRelationshipModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class SaveRelationshipModel extends RelationshipModel implements ISaveRelationshipModel {
  
  private static final long                              serialVersionUID            = 1L;
  
  protected List<? extends ISection>                     addedSections               = new ArrayList<>();
  protected List<IAddedSectionElementModel>              addedElements               = new ArrayList<>();
  protected List<String>                                 deletedSections             = new ArrayList<>();
  
  protected List<? extends IModifiedSectionElementModel> modifiedElements            = new ArrayList<>();
  protected List<IModifiedSectionModel>                  modifiedSections            = new ArrayList<>();
  protected List<IDeletedSectionElementModel>            deletedElements             = new ArrayList<>();
  
  protected List<IModifiedRelationshipPropertyModel>     modifiedAttributes          = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel>     deletedAttributes           = new ArrayList<>();
  
  protected List<IModifiedRelationshipPropertyModel>     modifiedTags                = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel>     deletedTags                 = new ArrayList<>();
  
  protected IAddedTabModel                               addedTab;
  protected String                                       deletedTab;
  
  @Override
  public List<? extends ISection> getAddedSections()
  {
    return this.addedSections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setAddedSections(List<? extends ISection> addedSections)
  {
    this.addedSections = addedSections;
  }
  
  @Override
  public List<String> getDeletedSections()
  {
    return this.deletedSections;
  }
  
  @Override
  public void setDeletedSections(List<String> deletedSectionIds)
  {
    this.deletedSections = deletedSectionIds;
  }
  
  @Override
  public List<? extends IModifiedSectionElementModel> getModifiedElements()
  {
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = AbstractModifiedSectionElementModel.class)
  @Override
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public List<IModifiedSectionModel> getModifiedSections()
  {
    if (modifiedSections == null) {
      modifiedSections = new ArrayList<>();
    }
    return modifiedSections;
  }
  
  @JsonDeserialize(contentAs = ModifiedSectionModel.class)
  @Override
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections)
  {
    this.modifiedSections = modifiedSections;
  }
  
  @Override
  public List<IDeletedSectionElementModel> getDeletedElements()
  {
    if (deletedElements == null) {
      return new ArrayList<>();
    }
    return deletedElements;
  }
  
  @JsonDeserialize(contentAs = DeletedSectionElementModel.class)
  @Override
  public void setDeletedElements(List<IDeletedSectionElementModel> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public List<IAddedSectionElementModel> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = AddedSectionElementModel.class)
  @Override
  public void setAddedElements(List<IAddedSectionElementModel> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getModifiedAttributes()
  {
    return modifiedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setModifiedAttributes(List<IModifiedRelationshipPropertyModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getDeletedAttributes()
  {
    return deletedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setDeletedAttributes(List<IModifiedRelationshipPropertyModel> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getModifiedTags()
  {
    return modifiedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setModifiedTags(List<IModifiedRelationshipPropertyModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getDeletedTags()
  {
    return deletedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setDeletedTags(List<IModifiedRelationshipPropertyModel> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public IRelationshipSide getSide1()
  {
    return entity.getSide1();
  }
  
  @JsonDeserialize(as = SaveRelationshipSide.class)
  @Override
  public void setSide1(IRelationshipSide side1)
  {
    entity.setSide1(side1);
  }
  
  @Override
  public IRelationshipSide getSide2()
  {
    return entity.getSide2();
  }
  
  @JsonDeserialize(as = SaveRelationshipSide.class)
  @Override
  public void setSide2(IRelationshipSide side2)
  {
    entity.setSide2(side2);
  }
  
  @Override
  public String getDeletedTab()
  {
    return deletedTab;
  }
  
  @Override
  public void setDeletedTab(String deletedTab)
  {
    this.deletedTab = deletedTab;
  }
  
  @Override
  public IAddedTabModel getAddedTab()
  {
    return addedTab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setAddedTab(IAddedTabModel addedTab)
  {
    this.addedTab = addedTab;
  }
}
