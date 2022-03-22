package com.cs.core.csexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByRelationshipFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author vallee
 */
public class CSEEntityByRelationshipFilter extends CSEEntityFilterNode implements ICSEEntityByRelationshipFilter {

  private ICSECouplingSource filteredObject;
  private ICSECouplingSource owner = null;
  private boolean complement = false;
  private ICSEProperty property;

  public CSEEntityByRelationshipFilter() {
    super(FilterNodeType.byRelationship);
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    filteredObject = source;
  }

  @Override
  public ICSECouplingSource getPropertyOwner() {
    return owner == null ? filteredObject : owner;
  }

  void setPropertyOwner(ICSECouplingSource owner) {
    this.owner = owner;
  }

  void setProperty(ICSEProperty prop) {
    this.property = prop;
  }

  @Override
  public ICSEProperty getProperty() {
    return property;
  }

  void setComplement(boolean status) {
    complement = status;
  }

  @Override
  public boolean getComplement() {
    return complement;
  }

  @Override
  public Collection<ICSEEntityFilterNode> getSimpleFilters() {
    return Arrays.asList(new ICSEEntityFilterNode[]{this});
  }

  @Override
  public Collection<String> getIncludingClassifiers() throws CSFormatException {
    return new HashSet<>(); // Empty
  }

  @Override
  public Collection<String> getExcludedClassifiers() throws CSFormatException {
    return new HashSet<>(); // Empty
  }

  @Override
  public Collection<String> getContainedProperties() throws CSFormatException {
    return new HashSet<>(); // Empty
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    IJSONContent json = super.toJSON();
    json.setField("entity", filteredObject.toString());
    json.setField("owner", owner.toString());
    json.setField("property", property.toString());
    json.setField("compement", complement);
    return json;
  }

  @Override
  public String toString() {
    return String.format(
            "%s %s belongsto %s.%s%s %s", hasNot() ? "not (" : "",
            filteredObject.toString(), owner.toString(), property.toString(),
            complement ? ".complement" : "", hasNot() ? ")" : "");
  }

}
