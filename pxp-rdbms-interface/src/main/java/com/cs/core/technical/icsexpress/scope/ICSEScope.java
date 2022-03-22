package com.cs.core.technical.icsexpress.scope;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.List;

import java.util.Set;

/**
 * Definition of a CSE scope
 *
 * @author vallee
 */
public interface ICSEScope {

  /**
   * @return the catalog codes applicable to this scope
   */
  public Set<ScopeBaseType> getBaseTypes();

  /**
   * @return the catalog codes applicable to this scope
   */
  public Set<String> getCatalogCodes();

  /**
   * @return the organization codes applicable to this scope
   */
  public Set<String> getOrganizationCodes();

  /**
   * @return the locale IDs applicable to this scope
   */
  public List<String> getLocaleIDs();
  
  /**
   * @return the base entity IIDs applicable to this scope
   */
  public Set<Long> getEntityIIDs();

  /**
   * @return true when the language scope includes base locale ID
   */
  public boolean applyToBaseLocale();

  /**
   * @return the entity conditions (filters) applicable to this scope
   */
  public ICSEEntityFilterNode getEntityFilter();

  /**
   * @return the entity conditions (filters) applicable to this scope
   */
  public Set<String> getEndpointCodes();

  /**
   * @return the JSON compilation result of the scope
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

  public enum ScopeBaseType {

    $article(BaseType.ARTICLE), $extension(BaseType.RELATIONSHIP_EXTENSION), $asset(BaseType.ASSET),
    $textasset(BaseType.TEXT_ASSET), $bundle(BaseType.BUNDLE), $set(BaseType.SET),
    $vcatalog(BaseType.VIRTUAL_CATALOG), $supplier(BaseType.SUPPLIER),
    $taxonomy(BaseType.TAXONOMY), $target(BaseType.TARGET);

    private final BaseType baseType;

    private ScopeBaseType(BaseType type) {
      baseType = type;
    }

    public BaseType getBaseType() {
      return baseType;
    }
  }

}
