package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.asset.AssetCollectionKlassSaveModel;
import com.cs.core.config.interactor.model.asset.AssetKlassSaveModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.configdetails.ModifiedSectionModel;
import com.cs.core.config.interactor.model.supplier.SupplierSaveModel;
import com.cs.core.config.interactor.model.target.TargetCollectionKlassSaveModel;
import com.cs.core.config.interactor.model.target.TargetSaveModel;
import com.cs.core.config.interactor.model.textasset.TextAssetSaveModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.context.ModifiedContextKlassModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = ContentKlassSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.ContentKlass"),
    @Type(value = ProjectKlassSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.ProjectKlass"),
    @Type(value = AssetKlassSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.Asset"),
    @Type(value = CollectionKlassSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.CollectionKlass"),
    @Type(value = SetKlassSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.SetKlass"),
    @Type(value = AssetCollectionKlassSaveModel.class,
        name = "com.cs.core.config.interactor.entity.klass.AssetCollectionKlass"),
    @Type(value = TargetCollectionKlassSaveModel.class,
        name = "com.cs.core.config.interactor.entity.klass.TargetCollectionKlass"),
    @Type(value = TargetSaveModel.class, name = "com.cs.core.config.interactor.entity.klass.Market"),
    @Type(value = TextAssetSaveModel.class, name = "com.cs.core.config.interactor.entity.textasset.TextAsset"),
    @Type(value = SupplierSaveModel.class, name = "com.cs.core.config.interactor.entity.supplier.Supplier") })
public abstract class AbstractKlassSaveModel extends AbstractKlassModel implements IKlassSaveModel {
  
  private static final long                                       serialVersionUID                                 = 1L;
  
  protected List<? extends ISection>                              addedSections                                    = new ArrayList<>();
  protected List<String>                                          deletedSections                                  = new ArrayList<>();
  protected List<String>                                          addedDataRules;
  protected List<String>                                          deletedDataRules;
  protected List<? extends IModifiedSectionElementModel>          modifiedElements;
  protected List<IModifiedSectionModel>                           modifiedSections;
  protected IKlassContextModel                                    addedContexts;
  protected IKlassContextModel                                    deletedContexts;
  protected List<IAddedNatureRelationshipModel>                   addedRelationships;
  protected List<IModifiedNatureRelationshipModel>                modifiedRelationships;
  protected List<String>                                          deletedRelationships;
  protected List<String>                                          addedLifecycleStatusTags;
  protected List<String>                                          deletedLifecycleStatusTags;
  protected List<? extends ITag>                                  referencedTags                                   = new ArrayList<>();
  protected List<String>                                          addedTasks                                       = new ArrayList<>();
  protected List<String>                                          deletedTasks                                     = new ArrayList<>();
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable = new ArrayList<>();
  protected List<IContextKlassModel>                              addedContextKlasses;
  protected List<String>                                          deletedContextKlasses;
  protected List<IModifiedContextKlassModel>                      modifiedContextKlasses;
  protected IContextKlassModel                                    addedLanguageKlass;
  protected String                                                deletedLanguageKlass;
  protected IModifiedContextKlassModel                            modifiedLanguageKlass;
  protected ISaveRelationshipToExportModel                        relationshipExport;
  protected String                                                iconKey;


  
  public AbstractKlassSaveModel(IKlass klass)
  {
    super(klass);
  }
  
  @JsonIgnore
  @Override
  public List<? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonIgnore
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(List<? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<String> getAddedLifecycleStatusTags()
  {
    if (addedLifecycleStatusTags == null) {
      addedLifecycleStatusTags = new ArrayList<String>();
    }
    return addedLifecycleStatusTags;
  }
  
  @Override
  public void setAddedLifecycleStatusTags(List<String> addedLifecycleStatusTags)
  {
    this.addedLifecycleStatusTags = addedLifecycleStatusTags;
  }
  
  @Override
  public List<String> getDeletedLifecycleStatusTags()
  {
    if (deletedLifecycleStatusTags == null) {
      deletedLifecycleStatusTags = new ArrayList<String>();
    }
    return deletedLifecycleStatusTags;
  }
  
  @Override
  public void setDeletedLifecycleStatusTags(List<String> deletedLifecycleStatusTags)
  {
    this.deletedLifecycleStatusTags = deletedLifecycleStatusTags;
  }
  
  @Override
  public List<? extends ISection> getAddedSections()
  {
    if (addedSections == null) {
      addedSections = new ArrayList<>();
    }
    return this.addedSections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setAddedSections(List<? extends ISection> addedSections)
  {
    this.addedSections = addedSections;
  }
  
  /*  @Override
  public List<? extends ISectionSaveModel> getModifiedSections()
  {
    return this.modifiedSections;
  }
  
  @JsonDeserialize(contentAs = AbstractSectionSaveModel.class)
  @Override
  public void setModifiedSections(List<? extends ISectionSaveModel> modifiedSections)
  {
    this.modifiedSections = modifiedSections;
  }*/
  
  @Override
  public List<String> getDeletedSections()
  {
    if (deletedSections == null) {
      deletedSections = new ArrayList<>();
    }
    return this.deletedSections;
  }
  
  @Override
  public void setDeletedSections(List<String> deletedSectionIds)
  {
    this.deletedSections = deletedSectionIds;
  }
  
  @JsonIgnore
  @Override
  public List<? extends ISection> getSections()
  {
    // TODO : Bring it back later(Neo4j rest api uses old jackson which does not
    // use @JsonIgnore)
    // throw new RuntimeException("Not to be used on save Model");
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    // TODO : Bring it back later(Neo4j rest api uses old jackson which does not
    // use @JsonIgnore)
    // throw new RuntimeException("Not to be used on save Model");
  }
  
  @JsonIgnore
  @Override
  public List<String> getAllowedTypes()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    // TODO Auto-generated method stub
    super.setAllowedTypes(allowedTypes);
  }
  
  @Override
  public List<String> getAddedDataRules()
  {
    if (addedDataRules == null) {
      addedDataRules = new ArrayList<String>();
    }
    return addedDataRules;
  }
  
  @Override
  public void setAddedDataRules(List<String> addedDataRules)
  {
    this.addedDataRules = addedDataRules;
  }
  
  @Override
  public List<String> getDeletedDataRules()
  {
    if (deletedDataRules == null) {
      deletedDataRules = new ArrayList<String>();
    }
    return deletedDataRules;
  }
  
  @Override
  public void setDeletedDataRules(List<String> deletedDataRules)
  {
    this.deletedDataRules = deletedDataRules;
  }
  
  @Override
  public List<? extends IModifiedSectionElementModel> getModifiedElements()
  {
    if (modifiedElements == null) {
      modifiedElements = new ArrayList<>();
    }
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
  public IKlassContextModel getAddedContexts()
  {
    
    return addedContexts;
  }
  
  @Override
  @JsonDeserialize(as = KlassContextModel.class)
  public void setAddedContexts(IKlassContextModel addedContexts)
  {
    this.addedContexts = addedContexts;
  }
  
  @Override
  public IKlassContextModel getDeletedContexts()
  {
    
    return deletedContexts;
  }
  
  @Override
  @JsonDeserialize(as = KlassContextModel.class)
  public void setDeletedContexts(IKlassContextModel deletedContexts)
  {
    this.deletedContexts = deletedContexts;
  }
  
  @Override
  public List<IAddedNatureRelationshipModel> getAddedRelationships()
  {
    if (addedRelationships == null) {
      addedRelationships = new ArrayList<IAddedNatureRelationshipModel>();
    }
    return addedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = AddedNatureRelationshipModel.class)
  public void setAddedRelationships(List<IAddedNatureRelationshipModel> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<IModifiedNatureRelationshipModel> getModifiedRelationships()
  {
    if (modifiedRelationships == null) {
      modifiedRelationships = new ArrayList<IModifiedNatureRelationshipModel>();
    }
    return modifiedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedNatureRelationshipModel.class)
  public void setModifiedRelationships(List<IModifiedNatureRelationshipModel> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public List<String> getDeletedRelationships()
  {
    if (deletedRelationships == null) {
      deletedRelationships = new ArrayList<String>();
    }
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<String> getAddedTasks()
  {
    return addedTasks;
  }
  
  @Override
  public void setAddedTasks(List<String> addedTasks)
  {
    this.addedTasks = addedTasks;
  }
  
  @Override
  public List<String> getDeletedTasks()
  {
    return deletedTasks;
  }
  
  @Override
  public void setDeletedTasks(List<String> deletedTasks)
  {
    this.deletedTasks = deletedTasks;
  }
  
  @Override
  @JsonIgnore
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
  }
  
  @Override
  public List<IContextKlassModel> getAddedContextKlasses()
  {
    if (addedContextKlasses == null) {
      addedContextKlasses = new ArrayList<>();
    }
    return addedContextKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextKlassModel.class)
  public void setAddedContextKlasses(List<IContextKlassModel> addedContextKlasses)
  {
    this.addedContextKlasses = addedContextKlasses;
  }
  
  @Override
  public List<String> getDeletedContextKlasses()
  {
    if (deletedContextKlasses == null) {
      deletedContextKlasses = new ArrayList<>();
    }
    return deletedContextKlasses;
  }
  
  @Override
  public void setDeletedContextKlasses(List<String> deletedContextKlasses)
  {
    this.deletedContextKlasses = deletedContextKlasses;
  }
  
  @Override
  public List<IModifiedContextKlassModel> getModifiedContextKlasses()
  {
    if (modifiedContextKlasses == null) {
      modifiedContextKlasses = new ArrayList<>();
    }
    return modifiedContextKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedContextKlassModel.class)
  public void setModifiedContextKlasses(List<IModifiedContextKlassModel> modifiedContextKlasses)
  {
    this.modifiedContextKlasses = modifiedContextKlasses;
  }
  
  @Override
  public IContextKlassModel getAddedLanguageKlass()
  {
    return addedLanguageKlass;
  }
  
  @Override
  @JsonDeserialize(as = ContextKlassModel.class)
  public void setAddedLanguageKlass(IContextKlassModel addedLanguageKlass)
  {
    this.addedLanguageKlass = addedLanguageKlass;
  }
  
  @Override
  public String getDeletedLanguageKlass()
  {
    return deletedLanguageKlass;
  }
  
  @Override
  public void setDeletedLanguageKlass(String deletedLanguageKlass)
  {
    this.deletedLanguageKlass = deletedLanguageKlass;
  }
  
  @Override
  public IModifiedContextKlassModel getModifiedLanguageKlass()
  {
    return modifiedLanguageKlass;
  }
  
  @Override
  @JsonDeserialize(as = ModifiedContextKlassModel.class)
  public void setModifiedLanguageKlass(IModifiedContextKlassModel modifiedLanguageKlass)
  {
    this.modifiedLanguageKlass = modifiedLanguageKlass;
  }
  
  @Override
  public ISaveRelationshipToExportModel getRelationshipExport()
  {
    return relationshipExport;
  }
  
  @Override
  @JsonDeserialize(as = SaveRelationshipToExportModel.class)
  public void setRelationshipExport(ISaveRelationshipToExportModel relationshipExport)
  {
    this.relationshipExport = relationshipExport;
  }
}
