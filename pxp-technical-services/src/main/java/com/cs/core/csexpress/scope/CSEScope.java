package com.cs.core.csexpress.scope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class CSEScope implements ICSEScope {

  private final Set<ScopeBaseType> baseTypes = new HashSet<>();
  private final Set<String> catalogs = new HashSet<>();
  private final Set<String> organizations = new HashSet<>();
  private final List<String> localeIDs = new ArrayList<>();
  private final Set<String> endpointCodes = new HashSet<>();
  private ICSEEntityFilterNode filter = null;
  private boolean includeBaseLocale = false;
  private final Set<Long> baseEntityIIDs = new HashSet<>();

  CSEScope() {
  }
  public Set<String> getEndpointCodes()
  {
    return endpointCodes;
  }

  void addEndpointCode(String endpointCode)
  {
    endpointCodes.add(endpointCode);
  }


  @Override
  public Set<ScopeBaseType> getBaseTypes() {
    return baseTypes;
  }

  @Override
  public Set<String> getCatalogCodes() {
    return catalogs;
  }

  @Override
  public Set<String> getOrganizationCodes()
  {
    return organizations;
  }

  @Override
  public List<String> getLocaleIDs() {
    return localeIDs;
  }
  
  @Override
  public Set<Long> getEntityIIDs() {
    return baseEntityIIDs;
  }

  @Override
  public boolean applyToBaseLocale() {
    return includeBaseLocale;
  }

  @Override
  public ICSEEntityFilterNode getEntityFilter() {
    return filter;
  }

  /**
   * @param type a new added base type
   */
  void addBaseType(ScopeBaseType type) {
    baseTypes.add(type);
  }


  void addBaseEntity(Long baseEntity) {
    baseEntityIIDs.add(baseEntity);
  }
  /**
   * @param code a new added applicable catalog
   */
  void addCatalog(String code) {
    catalogs.add(code);
  }

  /**
   * @param localeID a new added applicable localeID
   */
  void addLocaleID(String localeID) {
    localeIDs.add(localeID);
  }

  /**
   *
   * @param organizationCode a new added applicable organization ID.
   */
  void addOrganizationCode(String organizationCode) {
    organizations.add(organizationCode);
  }

  /**
   * @param status overwritten status of base locale application
   */
  void setBaseLocale(boolean status) {
    includeBaseLocale = status;
  }

  void setEntityFilter(ICSEEntityFilterNode entityFilter) {
    filter = entityFilter;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    Set<String> baseTypeStr = new HashSet<>();
    for (ScopeBaseType type : baseTypes) {
      baseTypeStr.add(type.toString());
    }
    JSONContent json = new JSONContent();
    json.setStringArrayField("baseTypes", baseTypeStr);
    json.setStringArrayField("catalogs", catalogs);
    json.setStringArrayField("organizations", organizations);
    json.setStringArrayField("localeIDs", localeIDs);
    json.setLongArrayField("baseEntityIIDs", baseEntityIIDs);
    json.setStringArrayField("endpointCodes", endpointCodes);
    json.setField("baseLocaleID", includeBaseLocale);
    if (filter != null) {
      json.setField("filter", filter.toJSON());
    }
    return json;
  }

  @Override
  public String toString() {
    StringBuffer baseTypeStr = new StringBuffer();
    for (ScopeBaseType type : baseTypes) {
      baseTypeStr.append(type.toString())
              .append("|");
    }
    if (!baseTypes.isEmpty()) {
      baseTypeStr.setLength(baseTypeStr.length() - 1);
    }
    Set<String> locales = new HashSet<>(localeIDs);
    if (includeBaseLocale) {
      locales.add("$baselocale");
    }

    return String.format("$baseType = %s $ctlg = %s%s%s%s%s", baseTypeStr,
            String.join("|", catalogs),
            organizations.isEmpty() ? "" : " $org = " + String.join("|", organizations),
            locales.isEmpty() ? "" : " $locale = " + String.join("|", locales),
            baseEntityIIDs.isEmpty() ? "" : " $entityIIDs = " + Text.join("|", baseEntityIIDs),
            endpointCodes.isEmpty() ? "" : " $endpoint = " + String.join("|", endpointCodes),
            filter == null ? "" : " " + filter.toString());
  }

}
