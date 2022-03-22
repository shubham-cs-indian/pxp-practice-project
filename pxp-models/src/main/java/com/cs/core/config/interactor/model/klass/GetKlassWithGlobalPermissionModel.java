package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassWithGlobalPermissionModel implements IGetKlassWithGlobalPermissionModel {
  
  private static final long                                       serialVersionUID             = 1L;
  
  protected IKlass                                                klass;
  protected Map<String, IKlassInformationModel>                   referencedKlasses            = new HashMap<>();
  protected Map<String, ? extends IStructure>                     referencedStructures         = new HashMap<>();
  protected Map<String, IGlobalPermission>                        globalPermission;
  protected List<IDefaultValueChangeModel>                        defaultValuesDiff            = new ArrayList<>();
  protected List<String>                                          deletedNatureRelationshipIds = new ArrayList<>();
  protected List<String>                                          deletedRelationshipIds       = new ArrayList<>();
  protected Map<String, ? extends ITag>                           referencedTags               = new HashMap<>();
  protected Map<String, ? extends IAttribute>                     referencedAttributes         = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>            referencedRelationships;
  protected Map<String, List<String>>                             deletedPropertiesFromSource;
  protected IRelationshipPropertiesToInheritModel                 addedElements;
  protected IRelationshipPropertiesToInheritModel                 deletedElements;
  protected IRelationshipPropertiesToInheritModel                 modifiedElements;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutCreateEnable;
  protected Map<String, String>                                   referencedContexts;
  protected Map<String, String>                                   referencedEvents;
  protected Map<String, String>                                   referencedTasks;
  protected Map<String, String>                                   referencedDataRules;
  protected Map<String, IIdLabelModel>                            referencedTabs;
  
  // Map of context id vs context label
  @Override
  public Map<String, String> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  public void setReferencedContexts(Map<String, String> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, ? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, ? extends IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public IKlass getKlass()
  {
    return this.klass;
  }
  
  @Override
  public void setKlass(IKlass klass)
  {
    this.klass = klass;
  }
  
  @Override
  public Map<String, IKlassInformationModel> getReferencedKlasses()
  {
    return this.referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  public void setReferencedKlasses(Map<String, IKlassInformationModel> klasses)
  {
    this.referencedKlasses = klasses;
  }
  
  @Override
  public Map<String, ? extends IStructure> getReferencedStructures()
  {
    return referencedStructures;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures)
  {
    this.referencedStructures = referencedStructures;
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    if (globalPermission == null) {
      globalPermission = new HashMap<String, IGlobalPermission>();
    }
    return globalPermission;
  }
  
  @JsonDeserialize(contentAs = GlobalPermission.class)
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  ;
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getAddedElements()
  {
    return addedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setAddedElements(IRelationshipPropertiesToInheritModel addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getModifiedElements()
  {
    return modifiedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setModifiedElements(IRelationshipPropertiesToInheritModel modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getDeletedElements()
  {
    return deletedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setDeletedElements(IRelationshipPropertiesToInheritModel deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    if (contextsWithAutCreateEnable == null) {
      contextsWithAutCreateEnable = new ArrayList<>();
    }
    return contextsWithAutCreateEnable;
  }
  
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.contextsWithAutCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @Override
  public Map<String, String> getReferencedEvents()
  {
    return referencedEvents;
  }
  
  @Override
  public void setReferencedEvents(Map<String, String> referencedEvents)
  {
    this.referencedEvents = referencedEvents;
  }
  
  @Override
  public Map<String, String> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @Override
  public void setReferencedTasks(Map<String, String> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
  
  @Override
  public Map<String, String> getReferencedDataRules()
  {
    return referencedDataRules;
  }
  
  @Override
  public void setReferencedDataRules(Map<String, String> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedTabs()
  {
    return referencedTabs;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelModel.class)
  public void setReferencedTabs(Map<String, IIdLabelModel> referencedTabs)
  {
    this.referencedTabs = referencedTabs;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
}
