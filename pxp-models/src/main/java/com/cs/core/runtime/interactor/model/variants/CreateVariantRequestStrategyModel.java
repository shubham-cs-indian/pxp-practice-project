package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextPropertiesModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextPropertiesModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedVariantContextModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateVariantRequestStrategyModel extends CreateVariantModel
    implements ICreateVariantRequestStrategyModel {
  
  private static final long                                serialVersionUID             = 1L;
  
  protected Map<String, IReferencedSectionElementModel>    referencedElements;
  protected Map<String, IReferencedVariantContextModel>    referencedVariantContext;
  protected String                                         organizationId;
  protected String                                         physicalCatalogId;
  protected String                                         logicalCatalogId;
  protected String                                         systemId;
  protected String                                         endpointId;
  protected Map<String, List<String>>                      typeIdIdentifierAttributeIds = new HashMap<>();
  protected Map<String, IAttribute>                        referencedAttributes;
  protected List<IDataRuleModel>                           referencedDataRules;
  protected Map<String, IReferencedContextPropertiesModel> referencedContextProperties;
  protected Map<String, ITag>                              referencedTags;
  
  public CreateVariantRequestStrategyModel()
  {
  }
  
  public CreateVariantRequestStrategyModel(ICreateVariantModel model)
  {
    this.id = model.getId();
    this.originalInstanceId = model.getOriginalInstanceId();
    this.attributeIds = model.getAttributeIds();
    this.attributes = model.getAttributes();
    this.contextId = model.getContextId();
    this.contextInstanceId = model.getContextInstanceId();
    this.contextTags = model.getContextTags();
    this.createdBy = model.getCreatedBy();
    this.createdOn = model.getCreatedOn();
    this.isDuplicateVariantAllowed = model.getIsDuplicateVariantAllowed();
    // this.isFromExternalSource = model.getIsFromExternalSource();
    this.lastModified = model.getLastModified();
    this.lastModifiedBy = model.getLastModifiedBy();
    this.linkedInstances = model.getLinkedInstances();
    this.metadata = model.getMetadata();
    this.name = model.getName();
    this.parentId = model.getParentId();
    this.types = model.getTypes();
    this.variantId = model.getVariantInstanceId();
    this.tags = model.getTags();
    this.timeRange = model.getTimeRange();
    this.templateId = model.getTemplateId();
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
  public Map<String, IReferencedVariantContextModel> getReferencedVariantContext()
  {
    return referencedVariantContext;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextModel.class)
  public void setReferencedVariantContext(
      Map<String, IReferencedVariantContextModel> referencedVariantContext)
  {
    this.referencedVariantContext = referencedVariantContext;
  }
  
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  public String getSystemId()
  {
    return systemId;
  }
  
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
  }
  
  public String getEndpointId()
  {
    return endpointId;
  }
  
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public List<IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new ArrayList<>();
    }
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, IReferencedContextPropertiesModel> getReferencedContextProperties()
  {
    if (referencedContextProperties == null) {
      referencedContextProperties = new HashMap<>();
    }
    return referencedContextProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedContextPropertiesModel.class)
  @Override
  public void setReferencedContextProperties(
      Map<String, IReferencedContextPropertiesModel> referencedContextProperties)
  {
    this.referencedContextProperties = referencedContextProperties;
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
}
