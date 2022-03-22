package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.util.Set;

/**
 * A base entity is made of: - a catalog object ID - a base type of entity - a base locale - a classifier (the nature class of the base
 * entity) - a source catalog IID - extensions - created and modified tracking information
 *
 * @author vallee
 */
public interface IBaseEntityIDDTO extends IRootDTO, Comparable {

  /**
   * @return the object RDBMS IID
   */
  public long getBaseEntityIID();

  ;
  
  /**
   * @return the type of entity
   */
  public BaseType getBaseType();

  /**
   * @return the business ID of this entity
   */
  public String getBaseEntityID();

  /**
   * @return the catalog information where this entity is located
   */
  public ICatalogDTO getCatalog();

  /**
   * @return the base locale
   */
  public String getBaseLocaleID();
  
  /**
   * @param baselocalID overwritten other baselocalID
   */
  public void setBaseLocaleID(String baselocalID);

  /**
   * @return the localeCatalog of the entity (built on the entity catalog and its base locale)
   */
  public ILocaleCatalogDTO getLocaleCatalog();

  /**
   * @return the nature classifier
   */
  public IClassifierDTO getNatureClassifier();
  /**
   * set nature classifier
   */
  void setNatureClassifier(IClassifierDTO natureClass);
  
  /**
   * @return the set of additional classifiers attached to the entity
   */
  public Set<IClassifierDTO> getOtherClassifiers();

  /**
   * @param classifiers overwritten other classifiers IID
   */
  public void setOtherClassifierIIDs(IClassifierDTO... classifiers);

  /**
   * @return the child level of this entity
   */
  public int getChildLevel();

  /**
   * @return the contextual object of this entity
   */
  public IContextualDataDTO getContextualObject();
  
  /**
   * @param contextualObject the contextual object of this entity
   */
  public void setContextualObject(IContextualDataDTO contextualObject);
  
  /**
   * @return the original organization code where base entity was first created
   */
  public String getSourceOrganizationCode(); 

  /**
   * @return the default image entity IID when defined
   */
  public long getDefaultImageIID();

  /**
   * @param imageIID overwritten default image IID
   */
  public void setDefaultImageIID(long imageIID);

  /**
   * @return the hash code attached to that entity
   */
  public String getHashCode();

  /**
   * @param hashCode overwritten hash code of the entity
   */
  public void setHashCode(String hashCode);

  public enum BaseType {

    UNDEFINED("??"), RELATIONSHIP_EXTENSION("EXT"), ARTICLE("ART"), PID("PID"), ASSET("ASS"),
    TEXT_ASSET("TXT"), BUNDLE("BDL"), SET("SET"),
    VIRTUAL_CATALOG("VCT"), SUPPLIER("SUP"), TAXONOMY("TAX"), TARGET("TGT"),  ATTACHMENT("ATT"), 
    TEMPLATE("TEM"), PROMOTION("PMN");

    private final String idPrefix;

    private BaseType(String prfx) {
      this.idPrefix = prfx;
    }

    public String getPrefix() {
      return idPrefix;
    }
    private static final BaseType[] values = values();

    public static BaseType valueOf(int ordinal) {
      return values[ordinal];
    }

    public static BaseType getBaseTypeByName(String name)
    {
      for (BaseType value : values) {
        if (value.name().equals(name)) {
          return value;
        }
      }
      return BaseType.UNDEFINED;
    }
  }

  public enum EmbeddedType {

    UNDEFINED, CONTEXTUAL_CLASS, GTIN_CLASS,
    TECHNICAL_IMAGE_VARIANT_CLASS;

    private static final EmbeddedType[] values = values();

    public static EmbeddedType valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  
}
