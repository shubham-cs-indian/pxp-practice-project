package com.cs.core.csexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByContextFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;

/**
 *
 * @author vallee
 */
public class CSEEntityByContextFilter extends CSEEntityFilterNode implements ICSEEntityByContextFilter {

  private ICSECouplingSource filteredObject;
  private final Set<ICSEObject> contexts = new HashSet<>();

  CSEEntityByContextFilter() {
    super(FilterNodeType.byContext);
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    filteredObject = source;
  }

  void addContext(ICSEObject context) {
    contexts.add(context);
  }

  @Override
  public Collection<ICSEObject> getContexts() {
    return contexts;
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
    json.setField("op", "has");
    JSONArray contextsJSON = new JSONArray();
    contexts.forEach(
            (ICSEObject context) -> {
              contextsJSON.add(context.toString());
            });
    json.setField("contexts", contextsJSON);
    return json;
  }

  @Override
  public String toString() {
    StringBuffer contextStr = new StringBuffer();
    contexts.forEach(
            (ICSEObject context) -> {
              contextStr.append(context.toString()).append(" | ");
            });
    if (!contexts.isEmpty()) {
      contextStr.setLength(contextStr.length() - 3);
    }
    return String.format(
            "%s %s has %s %s",
            hasNot() ? "not (" : "",
            filteredObject.toString(),
            contextStr,
            hasNot() ? ")" : "");
  }

}
