package com.cs.core.csexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByExpiryFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author vallee
 */
public class CSEEntityByExpiryFilter extends CSEEntityFilterNode implements ICSEEntityByExpiryFilter {

  private ICSECouplingSource filteredObject;
  private boolean  isExpired = false;

  public CSEEntityByExpiryFilter() {
    super(FilterNodeType.byExpiry);
  }

  void setIsExpired(boolean isExpired) {
    this.isExpired = isExpired;
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override public Boolean getIsExpired()
  {
    return isExpired;
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
    json.setField("expires", isExpired);
    return json;
  }

  @Override
  public String toString()
  {
    return String.format("isExpired = %s", isExpired);
  }

}
