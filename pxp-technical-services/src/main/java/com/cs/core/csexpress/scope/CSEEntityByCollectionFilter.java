package com.cs.core.csexpress.scope;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByCollectionFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByContextFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vallee
 */
public class CSEEntityByCollectionFilter extends CSEEntityFilterNode implements ICSEEntityByCollectionFilter {

  private ICSECouplingSource filteredObject;
  private ICSEObject collection = new CSEObject(ICSEElement.CSEObjectType.Collection);

  public CSEEntityByCollectionFilter() {
    super(FilterNodeType.byCollection);
  }

  public void setCollection(ICSEObject collection)
  {
    this.collection = collection;
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override
  public ICSEObject getCollection()
  {
    return collection;
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
    json.setField("op", "has");
    JSONArray collectionJSON = new JSONArray();
    collectionJSON.add(collection.toString());
    json.setField("collection", collectionJSON);
    return json;
  }

  @Override
  public String toString() {
    StringBuffer collectionStr = new StringBuffer();
              collectionStr.append(collection.toString());
    return String.format(
            "%s %s belongsto %s %s",
            hasNot() ? "not (" : "",
            filteredObject.toString(),
            collectionStr,
            hasNot() ? ")" : "");
  }

}
