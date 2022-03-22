package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextPropertiesModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;

import java.util.List;
import java.util.Map;

public interface ICreateVariantRequestStrategyModel extends ICreateVariantModel {
  
  public static final String REFERENCED_ELEMENTS            = "referencedElements";
  public static final String REFERENCED_VARIANT_CONTEXT     = "referencedVariantContext";
  public static final String ORGANIZATION_ID                = "organizationId";
  public static final String ENDPOINT_ID                    = "endpointId";
  public static final String PHYSICAL_CATALOG_ID            = "physicalCatalogId";
  public static final String LOGICAL_CATALOG_ID             = "logicalCatalogId";
  public static final String SYSTEM_ID                      = "systemId";
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS = "typeIdIdentifierAttributeIds";
  public static final String REFERENCED_ATTRIBUTES          = "referencedAttributes";
  public static final String REFERENCED_DATA_RULES          = "referencedDataRules";
  public static final String REFERENCED_CONTEXT_PROPERTIES  = "referencedContextProperties";
  public static final String REFERENCED_TAGS                = "referencedTags";
  
  // key:propertyId[attributeId, tagId, roleId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public Map<String, IReferencedVariantContextModel> getReferencedVariantContext();
  
  public void setReferencedVariantContext(
      Map<String, IReferencedVariantContextModel> referencedVariantContext);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public Map<String, IReferencedContextPropertiesModel> getReferencedContextProperties();
  
  // key:contextKlassId
  public void setReferencedContextProperties(
      Map<String, IReferencedContextPropertiesModel> referencedContextProperties);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
