package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.configdetails.DeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.configdetails.ModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.AbstractModifiedSectionElementModel;
import com.cs.core.config.interactor.model.klass.AddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveStaticCollectionDetailsModel implements ISaveStaticCollectionDetailsModel {
  
  private static final long                              serialVersionUID = 1L;
  
  protected String                                       id;
  protected String                                       label;
  protected List<? extends ISection>                     addedSections    = new ArrayList<>();
  protected List<? extends IModifiedSectionElementModel> modifiedElements = new ArrayList<>();
  protected List<IModifiedSectionModel>                  modifiedSections = new ArrayList<>();
  protected List<String>                                 deletedSections  = new ArrayList<>();
  protected List<IAddedSectionElementModel>              addedElements    = new ArrayList<>();
  protected List<IDeletedSectionElementModel>            deletedElements  = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
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
}
