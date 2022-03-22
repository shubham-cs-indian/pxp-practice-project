package com.cs.core.csexpress.scope;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByClassifierFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONArray;

/**
 * Implementation of a filter by matching to classifiers
 *
 * @author vallee
 */
public class CSEEntityByClassifierFilter extends CSEEntityFilterNode implements ICSEEntityByClassifierFilter {

  private ICSECouplingSource filteredObject;
  private FilterOperator operator;
  private final Set<ICSECouplingSource> classifiers = new HashSet<>();
  private boolean includesNature = false;
  CSEEntityByClassifierFilter() {
    super(FilterNodeType.byClassifier);
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override
  public FilterOperator getOperator() {
    return operator;
  }

  @Override
  public Collection<ICSECouplingSource> getClassifiers() {
    return classifiers;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    filteredObject = source;
  }

  void addClassifier(ICSECouplingSource classifier) {
    if (classifier.isPredefined()) // covers only the case for nature class
    {
      includesNature = true;
    }
    classifiers.add(classifier);
  }

  void setOperator(FilterOperator filterOperator) {
    operator = filterOperator;
  }

  @Override
  public boolean containsNatureClass() {
    return includesNature;
  }

  @Override
  public Collection<ICSEEntityFilterNode> getSimpleFilters() {
    return Arrays.asList(new ICSEEntityFilterNode[]{this});
  }

  private Set<String> getClassifierCodes() {
    Set<String> codes = new HashSet<>();
    classifiers.forEach(
            (ICSECouplingSource classifier) -> {
              if (!classifier.isPredefined()) {
                codes.add(((CSECouplingSource) classifier).getSource().getCode());
              }
            });
    return codes;
  }

  @Override
  public Collection<String> getIncludingClassifiers() throws CSFormatException {
    if (hasNot()) {
      return new HashSet<>(); // Empty
    }
    return getClassifierCodes();
  }

  @Override
  public Collection<String> getExcludedClassifiers() throws CSFormatException {
    if (!hasNot()) {
      return new HashSet<>(); // Empty
    }
    return getClassifierCodes();
  }

  @Override
  public Collection<String> getContainedProperties() throws CSFormatException {
    return new HashSet<>(); // Empty
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    IJSONContent json = super.toJSON();
    json.setField("entity", filteredObject.toString());
    json.setField("op", operator);
    JSONArray classifiersJSON = new JSONArray();
    classifiers.forEach(
            (ICSECouplingSource classifier) -> {
              classifiersJSON.add(classifier.toString());
            });
    json.setField("classifiers", classifiersJSON);
    return json;
  }

  @Override
  public String toString() {
    StringBuffer classifiersStr = new StringBuffer();
    classifiers.forEach(
            (ICSECouplingSource classifier) -> {
              classifiersStr.append(classifier.toString()).append(" | ");
            });
    if (!classifiers.isEmpty()) {
      classifiersStr.setLength(classifiersStr.length() - 3);
    }
    return String.format(
            "%s %s %s %s %s",
            hasNot() ? "not (" : "",
            filteredObject.toString(),
            operator.toString(),
            classifiersStr,
            hasNot() ? ")" : "");
  }
}
