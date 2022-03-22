package com.cs.core.csexpress.definition;

import com.cs.core.csexpress.CSEElement;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import java.util.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author vallee
 */
public class CSEObject extends CSEElement implements ICSEObject {

  // Define here the set of keywords accept by type of object
  private static final Map<CSEObjectType, Keyword[]> KWMAP = new HashMap<>();

  static {
    Keyword[] UnknownKW = {};
    KWMAP.put(CSEObjectType.Unknown, UnknownKW);

    Keyword[] PropertyKW = {Keyword.$iid, Keyword.$type, Keyword.$cxt, Keyword.$tag,
      Keyword.$start, Keyword.$end, Keyword.$side, Keyword.$locale};
    KWMAP.put(CSEObjectType.Property, PropertyKW);

    Keyword[] ContextualObjectKW = {Keyword.$iid, Keyword.$cxt, Keyword.$type, Keyword.$tag,
      Keyword.$start, Keyword.$end};
    KWMAP.put(CSEObjectType.ContextualObject, ContextualObjectKW);

    Keyword[] RelationSideRecordKW = {Keyword.$iid, Keyword.$side, Keyword.$cxt, Keyword.$tag,
      Keyword.$start, Keyword.$end, Keyword.$type, Keyword.$side};
    KWMAP.put(CSEObjectType.RelationSideRecord, RelationSideRecordKW);

    Keyword[] TagsRecordKW = {Keyword.$iid, Keyword.$tag, Keyword.$prop, Keyword.$type, Keyword.$locale, Keyword.$isver};
    KWMAP.put(CSEObjectType.TagsRecord, TagsRecordKW);

    Keyword[] ValueRecordKW = {Keyword.$iid, Keyword.$locale, Keyword.$cxt, Keyword.$tag,
      Keyword.$date, Keyword.$parent, Keyword.$type, Keyword.$isver};
    KWMAP.put(CSEObjectType.ValueRecord, ValueRecordKW);

    Keyword[] EntityKW = {Keyword.$iid, Keyword.$ctlg, Keyword.$org, Keyword.$cxt,
      Keyword.$tag, Keyword.$start, Keyword.$end, Keyword.$type};
    KWMAP.put(CSEObjectType.Entity, EntityKW);

    Keyword[] TagValueKW = {Keyword.$prop, Keyword.$range, Keyword.$rangeof};
    KWMAP.put(CSEObjectType.TagValue, TagValueKW);

    Keyword[] TaskRecordKW = {Keyword.$iid};
    KWMAP.put(CSEObjectType.TaskRecord, TaskRecordKW);

    Keyword[] CollectionKW = { Keyword.$iid, Keyword.$ctlg, Keyword.$org, Keyword.$content, Keyword.$search };
    KWMAP.put(CSEObjectType.Collection, CollectionKW);

    Keyword[] ContextKW = {Keyword.$iid, Keyword.$type, Keyword.$start, Keyword.$end};
    KWMAP.put(CSEObjectType.Context, ContextKW);

    Keyword[] CatalogKW = {Keyword.$type, Keyword.$locale};
    KWMAP.put(CSEObjectType.Catalog, CatalogKW);

    Keyword[] ClassifierKW = {Keyword.$iid, Keyword.$type, Keyword.$parent};
    KWMAP.put(CSEObjectType.Classifier, ClassifierKW);

    Keyword[] UserKW = {Keyword.$iid};
    KWMAP.put(CSEObjectType.User, UserKW);

    Keyword[] TrackingKW = {Keyword.$iid, Keyword.$date, Keyword.$type};
    KWMAP.put(CSEObjectType.Tracking, TrackingKW);

    Keyword[] PropertyCollectionKW = {}; // not yet implemented
    KWMAP.put(CSEObjectType.PropertyCollection, PropertyCollectionKW);
    
    Keyword[] TabKW = {}; // not yet implemented
    KWMAP.put(CSEObjectType.Tab, TabKW);
    
    Keyword[] TaskKW = {Keyword.$type}; // not yet implemented
    KWMAP.put(CSEObjectType.Task, TaskKW);
    
    Keyword[] OrganizationKW = {Keyword.$type}; // not yet implemented
    KWMAP.put(CSEObjectType.Organization, OrganizationKW);
    
    Keyword[] RoleKW = {Keyword.$type}; // not yet implemented
    KWMAP.put(CSEObjectType.Role, RoleKW);
    
    Keyword[] LanguageKW = {Keyword.$type}; // not yet implemented
    KWMAP.put(CSEObjectType.LanguageConf, LanguageKW);

    Keyword[] CouplingSourceKW  = {Keyword.$source, Keyword.$target, Keyword.$type}; // not yet implemented
    KWMAP.put(CSEObjectType.CouplingSource, CouplingSourceKW);
    
    Keyword[] TranslationKW  = {Keyword.$locale, Keyword.$iid};
    KWMAP.put(CSEObjectType.Translation, TranslationKW);
    
    Keyword[] PropertyPermissionKW = {Keyword.$type}; 
    KWMAP.put(CSEObjectType.PropertyPermission, PropertyPermissionKW);
    
    Keyword[] RelationshipPermissionKW = {Keyword.$type}; 
    KWMAP.put(CSEObjectType.RelationshipPermission, RelationshipPermissionKW);
    
    Keyword[] PermissionKW = {Keyword.$type}; 
    KWMAP.put(CSEObjectType.Permission, PermissionKW);
  }

  private final CSEObjectType objectType;
  protected String code = "";

  /**
   * Constructor that transmits the type to super type
   *
   * @param type
   * @param objectType
   */
  public CSEObject(ElementType type, CSEObjectType objectType) {
    super(type);
    this.objectType = objectType;
  }

  /**
   * Constructor that takes indirectly the object as OBJECT
   *
   * @param objectType
   */
  public CSEObject(CSEObjectType objectType) {
    super(ElementType.OBJECT);
    this.objectType = objectType;
  }

  @Override
  public boolean accept(Keyword keyword) {
    Set<Keyword> set = new HashSet<>(Arrays.asList(KWMAP.get(objectType)));
    return set.contains(keyword);
  }

  @Override
  public CSEObjectType getObjectType() {
    return objectType;
  }

  @Override
  public String getCode() {
    return code;
  }

  /**
   * @param aCode overwritten code
   */
  public final void setCode(String aCode) {
    code = aCode;
  }

  @Override
  public long getIID() {
    if (specifications.containsKey(Keyword.$iid)) {
      return Long.parseLong(specifications.get(Keyword.$iid));
    }
    return 0;
  }

  /**
   * @param iid overwritten IID of this object
   */
  public void setIID(long iid) {
    if (iid >= 0) {
      specifications.put(Keyword.$iid, String.format("%d", iid));
    }
  }

  /**
   * @param siid overwritten IID of this object
   */
  public void setIID(String siid) {
    if (!siid.isEmpty()) {
      setIID(Long.parseLong(siid));
    }
  }

  @Override
  public int compareTo(Object t) {
    if (super.compareTo(t) == 0) {
      return 0;
    }
    if (!this.code.isEmpty()) {
      return this.code.compareTo(((CSEObject) t).code);
    }
    return (new Long(this.getIID())).compareTo(((CSEObject) t).getIID());
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && code.isEmpty() && getIID() == 0;
  }

  /**
   * @return the enclosing expression with meta + identification + context + specifications
   */
  protected StringBuffer enclosingExpression() {
    StringBuffer csExpression = new StringBuffer();
    if (!metaValues.isEmpty()) {
      csExpression.append(metaExpression()).append(' ');
    }
    if (!code.isEmpty()) {
      csExpression.append(code).append(' ');
      csExpression.append(specificationExpression());
    } else {
      csExpression.append(Keyword.$null).append(' ');
    }
    while (csExpression.charAt(csExpression.length() - 1) == ' ') {
      csExpression.setLength(csExpression.length() - 1);
    }
    return csExpression;
  }

  @Override
  public StringBuffer toStringBuffer() {
    StringBuffer csExpression = new StringBuffer().append('[');
    if (objectType != CSEObjectType.Property) {
      csExpression.append(objectType.letter()).append('>');
    }
    return csExpression.append(enclosingExpression()).append(']');
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(5, 7).append(code)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CSEObject other = (CSEObject) obj;
    if (!Objects.equals(this.code, other.code)) {
      return false;
    }
    return true;
  }

  /**
   * Force this object as null
   */
  void setNull() {
    setCode("");
    setIID(0);
  }

  @Override
  public boolean isNull() {
    return code.isEmpty();
  }
}
