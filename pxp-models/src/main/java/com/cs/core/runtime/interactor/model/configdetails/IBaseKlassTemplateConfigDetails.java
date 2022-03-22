package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBaseKlassTemplateConfigDetails extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES           = "referencedAttributes";
  public static final String REFERENCED_TAGS                 = "referencedTags";
  public static final String REFERENCED_PROPERTY_COLLECTIONS = "referencedPropertyCollections";
  public static final String REFERENCED_ELEMENTS             = "referencedElements";
  public static final String REFERENCED_PERMISSIONS          = "referencedPermissions";
  public static final String REFERENCED_ROLES                = "referencedRoles";
  public static final String VISIBLE_PROPERTY_IDS            = "visiblePropertyIds";
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS  = "typeIdIdentifierAttributeIds";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  // key:propertyCollectionId
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IBaseKlassTemplatePermissionModel getReferencedPermissions();
  
  public void setReferencedPermissions(IBaseKlassTemplatePermissionModel referencedPermissions);
  
  // key:roleId
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Set<String> getVisiblePropertyIds();
  
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
}
