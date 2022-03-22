package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassWithGlobalPermissionModel extends IModel {
  
  public static final String KLASS                                                = "klass";
  public static final String DEFAULT_VALUES_DIFF                                  = "defaultValuesDiff";
  public static final String DELETED_NATURE_RELATIONSHIP_IDS                      = "deletedNatureRelationshipIds";
  public static final String DELETED_RELATIONSHIP_IDS                             = "deletedRelationshipIds";
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_ATTRIBUTES                                = "referencedAttributes";
  public static final String DELETED_PROPERTIES_FROM_SOURCE                       = "deletedPropertiesFromSource";
  public static final String ADDED_ELEMENTS                                       = "addedElements";
  public static final String DELETED_ELEMENTS                                     = "deletedElements";
  public static final String MODIFIED_ELEMENTS                                    = "modifiedElements";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  public static final String REFERENCED_CONTEXTS                                  = "referencedContexts";
  public static final String REFERENCED_KLASSES                                   = "referencedKlasses";
  public static final String REFERENCED_EVENTS                                    = "referencedEvents";
  public static final String REFERENCED_TASKS                                     = "referencedTasks";
  public static final String REFERENCED_DATARULES                                 = "referencedDataRules";
  public static final String REFERENCED_TABS                                      = "referencedTabs";
  public static final String REFERENCED_RELATIONSHIPS                             = "referencedRelationships";
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public IKlass getKlass();
  
  public void setKlass(IKlass klass);
  
  public Map<String, IKlassInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IKlassInformationModel> ReferencedKlasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public Map<String, IGlobalPermission> getGlobalPermission();
  
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public List<String> getDeletedNatureRelationshipIds();
  
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds);
  
  public List<String> getDeletedRelationshipIds();
  
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds);
  
  public Map<String, ? extends ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags);
  
  public Map<String, ? extends IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships);
  
  /**
   * Map key -> entityId , Value -> klassIds (usecase : remove
   * propertyCollection From klass or remove property from propertyCollection)
   */
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public IRelationshipPropertiesToInheritModel getAddedElements();
  
  public void setAddedElements(IRelationshipPropertiesToInheritModel addedElements);
  
  public IRelationshipPropertiesToInheritModel getModifiedElements();
  
  public void setModifiedElements(IRelationshipPropertiesToInheritModel modifiedElements);
  
  public IRelationshipPropertiesToInheritModel getDeletedElements();
  
  public void setDeletedElements(IRelationshipPropertiesToInheritModel deletedElements);
  
  public Map<String, String> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, String> referencedContexts);
  
  public Map<String, String> getReferencedEvents();
  
  public void setReferencedEvents(Map<String, String> referencedEvents);
  
  public Map<String, String> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, String> referencedTasks);
  
  public Map<String, String> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, String> referencedDataRules);
  
  public Map<String, IIdLabelModel> getReferencedTabs();
  
  public void setReferencedTabs(Map<String, IIdLabelModel> referencedTabs);
}
