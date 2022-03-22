package com.cs.core.csexpress.scope;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByDuplicateFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author jamil.ahmad
 *
 */
public class CSEEntityByDuplicateFilter extends CSEEntityFilterNode implements ICSEEntityByDuplicateFilter {

  private ICSECouplingSource filteredObject;
  private boolean  isDuplicate = false;

  public CSEEntityByDuplicateFilter() {
    super(FilterNodeType.byDuplicate);
  }

  void setIsDuplicate(boolean isDuplicate) {
    this.isDuplicate = isDuplicate;
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override public Boolean getIsDuplicate()
  {
    return isDuplicate;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    filteredObject = source;
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
    json.setField("duplicate", isDuplicate);
    return json;
  }

  @Override
  public String toString()
  {
    return String.format("isDuplicate = %s", isDuplicate);
  }

}
