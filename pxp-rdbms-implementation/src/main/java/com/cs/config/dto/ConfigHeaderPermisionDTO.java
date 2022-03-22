package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigHeaderPermisionDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigHeaderPermisionDTO extends AbstractConfigJSONDTO implements IConfigHeaderPermisionDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.viewName, IPXON.PXONTag.viewName.toTag());
    KEY_MAP.put(ConfigTag.canEditName, IPXON.PXONTag.canEditName.toTag());
    KEY_MAP.put(ConfigTag.viewIcon, IPXON.PXONTag.viewIcon.toTag());
    KEY_MAP.put(ConfigTag.canChangeIcon, IPXON.PXONTag.canChangeIcon.toTag());
    KEY_MAP.put(ConfigTag.viewPrimaryType, IPXON.PXONTag.viewPrimaryType.toTag());
    KEY_MAP.put(ConfigTag.canEditPrimaryType, IPXON.PXONTag.canEditPrimaryType.toTag());
    
    KEY_MAP.put(ConfigTag.viewAdditionalClasses, IPXON.PXONTag.viewAdditionalClasses.toTag());
    KEY_MAP.put(ConfigTag.canAddClasses, IPXON.PXONTag.canAddClasses.toTag());
    KEY_MAP.put(ConfigTag.canDeleteClasses, IPXON.PXONTag.canDeleteClasses.toTag());
    KEY_MAP.put(ConfigTag.viewTaxonomies, IPXON.PXONTag.viewTaxonomies.toTag());
    KEY_MAP.put(ConfigTag.canAddTaxonomy, IPXON.PXONTag.canAddTaxonomy.toTag());
    KEY_MAP.put(ConfigTag.canDeleteTaxonomy, IPXON.PXONTag.canDeleteTaxonomy.toTag());
    KEY_MAP.put(ConfigTag.viewStatusTags, IPXON.PXONTag.viewStatusTags.toTag());
    KEY_MAP.put(ConfigTag.canEditStatusTag, IPXON.PXONTag.canEditStatusTag.toTag());
    
    KEY_MAP.put(ConfigTag.viewCreatedOn, IPXON.PXONTag.viewCreatedOn.toTag());
    KEY_MAP.put(ConfigTag.viewLastModifiedBy, IPXON.PXONTag.viewLastModifiedBy.toTag());
    
   
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigHeaderPermisionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    // No CSE element
  }
  
  @Override
  public String getEntityId()
  {
    return getString(ConfigTag.entityId);
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    setString(ConfigTag.entityId, entityId);
  }
  
  @Override
  public Boolean getViewName()
  {
    return getBoolean(ConfigTag.viewName);
  }
  
  @Override
  public void setViewName(Boolean viewName)
  {
    setBoolean(ConfigTag.viewName, viewName);
  }
  
  @Override
  public Boolean getCanEditName()
  {
    return getBoolean(ConfigTag.canEditName);
  }
  
  @Override
  public void setCanEditName(Boolean canEditName)
  {
    setBoolean(ConfigTag.canEditName, canEditName);
  }
  
  @Override
  public Boolean getViewIcon()
  {
    return getBoolean(ConfigTag.viewIcon);
  }
  
  @Override
  public void setViewIcon(Boolean viewIcon)
  {
    setBoolean(ConfigTag.viewIcon, viewIcon);
  }
  
  @Override
  public Boolean getCanChangeIcon()
  {
    return getBoolean(ConfigTag.canChangeIcon);
  }
  
  @Override
  public void setCanChangeIcon(Boolean canChangeIcon)
  {
    setBoolean(ConfigTag.canChangeIcon, canChangeIcon);
  }
  
  @Override
  public Boolean getViewPrimaryType()
  {
    return getBoolean(ConfigTag.viewPrimaryType);
  }
  
  @Override
  public void setViewPrimaryType(Boolean viewPrimaryType)
  {
    setBoolean(ConfigTag.viewPrimaryType, viewPrimaryType);
  }
  
  @Override
  public Boolean getCanEditPrimaryType()
  {
    return getBoolean(ConfigTag.canEditPrimaryType);
  }
  
  @Override
  public void setCanEditPrimaryType(Boolean canEditPrimaryType)
  {
    setBoolean(ConfigTag.canEditPrimaryType, canEditPrimaryType);
  }
  
  @Override
  public Boolean getViewAdditionalClasses()
  {
    return getBoolean(ConfigTag.viewAdditionalClasses);
  }
  
  @Override
  public void setViewAdditionalClasses(Boolean viewAdditionalClasses)
  {
    setBoolean(ConfigTag.viewAdditionalClasses, viewAdditionalClasses);
  }
  
  @Override
  public Boolean getCanAddClasses()
  {
    return getBoolean(ConfigTag.canAddClasses);
  }
  
  @Override
  public void setCanAddClasses(Boolean canAddClasses)
  {
    setBoolean(ConfigTag.canAddClasses, canAddClasses);
  }
  
  @Override
  public Boolean getCanDeleteClasses()
  {
    return getBoolean(ConfigTag.canDeleteClasses);
  }
  
  @Override
  public void setCanDeleteClasses(Boolean canDeleteClasses)
  {
    setBoolean(ConfigTag.canDeleteClasses, canDeleteClasses);
  }
  
  @Override
  public Boolean getViewTaxonomies()
  {
    return getBoolean(ConfigTag.viewTaxonomies);
  }
  
  @Override
  public void setViewTaxonomies(Boolean viewTaxonomies)
  {
    setBoolean(ConfigTag.viewTaxonomies, viewTaxonomies);
  }
  
  @Override
  public Boolean getCanAddTaxonomy()
  {
    return getBoolean(ConfigTag.canAddTaxonomy);
  }
  
  @Override
  public void setCanAddTaxonomy(Boolean canAddTaxonomy)
  {
    setBoolean(ConfigTag.canAddTaxonomy, canAddTaxonomy);
  }
  
  @Override
  public Boolean getCanDeleteTaxonomy()
  {
    return getBoolean(ConfigTag.canDeleteTaxonomy);
  }
  
  @Override
  public void setCanDeleteTaxonomy(Boolean canDeleteTaxonomy)
  {
    setBoolean(ConfigTag.canDeleteTaxonomy, canDeleteTaxonomy);
  }
  
  @Override
  public Boolean getViewStatusTags()
  {
    return getBoolean(ConfigTag.viewStatusTags);
  }
  
  @Override
  public void setViewStatusTags(Boolean viewStatusTags)
  {
    setBoolean(ConfigTag.viewStatusTags, viewStatusTags);
  }
  
  @Override
  public Boolean getCanEditStatusTag()
  {
    return getBoolean(ConfigTag.canEditStatusTag);
  }
  
  @Override
  public void setCanEditStatusTag(Boolean canEditStatusTag)
  {
    setBoolean(ConfigTag.canEditStatusTag, canEditStatusTag);
  }
  
  @Override
  public Boolean getViewCreatedOn()
  {
    return getBoolean(ConfigTag.viewCreatedOn);
  }
  
  @Override
  public void setViewCreatedOn(Boolean viewCreatedOn)
  {
    setBoolean(ConfigTag.viewCreatedOn, viewCreatedOn);
  }
  
  @Override
  public Boolean getViewLastModifiedBy()
  {
    return getBoolean(ConfigTag.viewLastModifiedBy);
  }
  
  @Override
  public void setViewLastModifiedBy(Boolean viewLastModifiedBy)
  {
    setBoolean(ConfigTag.viewLastModifiedBy, viewLastModifiedBy);
  }
  
}
