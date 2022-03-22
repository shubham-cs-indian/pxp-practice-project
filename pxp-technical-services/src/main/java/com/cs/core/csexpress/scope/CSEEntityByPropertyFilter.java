package com.cs.core.csexpress.scope;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByPropertyFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.*;
import org.json.simple.JSONArray;

/**
 * Implementation of a filter per property(ies) containment
 *
 * @author vallee
 */
public class CSEEntityByPropertyFilter extends CSEEntityFilterNode
        implements ICSEEntityByPropertyFilter {

  private final PropertyFilter filter;
  private final Set<ICSEProperty> properties = new HashSet<>();
  private ICSECouplingSource filteredObject = new CSECouplingSource(
          ICSECouplingSource.Predefined.$entity);

  public CSEEntityByPropertyFilter(PropertyFilter filter) {
    super(FilterNodeType.byProperty);
    this.filter = filter;
  }

  /**
   * @param property added property to the filter conditions
   */
  void addProperty(ICSEProperty property) {
    properties.add(property);
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
  public Collection<ICSEProperty> getProperties() {
    return properties;
  }

  @Override
  public Collection<ICSEEntityFilterNode> getSimpleFilters() {
    return Arrays.asList(new ICSEEntityFilterNode[]{this});
  }

  @Override
  public Collection<String> getIncludingClassifiers() throws CSFormatException {
    return new ArrayList<>(); // empty
  }

  @Override
  public Collection<String> getExcludedClassifiers() throws CSFormatException {
    return new ArrayList<>(); // empty
  }

  @Override
  public PropertyFilter getPropertyFilter() {
    return filter;
  }

  @Override
  public Collection<String> getContainedProperties() throws CSFormatException {
    Set<String> propertyCodes = new HashSet<>();
    properties.forEach(property -> {
      propertyCodes.add(property.getCode());
    });
    return propertyCodes;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    IJSONContent json = super.toJSON();
    json.setField("entity", filteredObject.toString());
    json.setField("op", filter);
    JSONArray propertiesJSON = new JSONArray();
    properties.forEach((ICSEProperty property) -> {
      propertiesJSON.add(property.toString());
    });
    json.setField("properties", propertiesJSON);
    return json;
  }

  @Override
  public String toString() {
    StringBuffer propertyStr = new StringBuffer();
    properties.forEach((ICSEProperty property) -> {
      propertyStr.append(property.toString())
              .append(" | ");
    });
    if (!properties.isEmpty()) {
      propertyStr.setLength(propertyStr.length() - 3);
    }
    return String.format("%s %s %s %s %s", hasNot() ? "not (" : "", filteredObject.toString(),
            filter, propertyStr, hasNot() ? ")" : "");
  }
}
