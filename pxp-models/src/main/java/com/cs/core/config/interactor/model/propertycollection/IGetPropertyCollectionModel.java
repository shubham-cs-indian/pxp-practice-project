package com.cs.core.config.interactor.model.propertycollection;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.attributiontaxonomy.IMasterTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetPropertyCollectionModel extends IPropertyCollection, IConfigResponseWithAuditLogModel {
  
  public static final String REFERENCED_TAGS          = "referencedTags";
  public static final String REFERENCED_ATTRIBUTES    = "referencedAttributes";
  public static final String REFERENCED_ROLES         = "referencedRoles";
  public static final String REFERENCED_RELATIONSHIPS = "referencedRelationships";
  public static final String COMPLEX_ATTRIBUTE_IDS    = "complexAttributeIds";
  public static final String REFERENCED_TAXONOMIES    = "referencedTaxonomies";
  public static final String TAB                      = "tab";
  
  public List<ITag> getReferencedTags();
  
  public void setReferencedTags(List<ITag> referencedTags);
  
  public List<IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(List<IAttribute> referencedAttributes);
  
  public List<IRole> getReferencedRoles();
  
  public void setReferencedRoles(List<IRole> referencedRoles);
  
  public List<String> getComplexAttributeIds();
  
  public void setComplexAttributeIds(List<String> complexAttributeIds);
  
  public List<IRelationship> getReferencedRelationships();
  
  public void setReferencedRelationships(List<IRelationship> referencedRelationships);
  
  public List<IMasterTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(List<IMasterTaxonomyModel> referencedTaxonomies);
  
  public IIdLabelCodeModel getTab();
  
  public void setTab(IIdLabelCodeModel tab);
}
