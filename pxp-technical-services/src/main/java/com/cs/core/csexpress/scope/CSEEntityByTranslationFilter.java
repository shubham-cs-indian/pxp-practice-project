package com.cs.core.csexpress.scope;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByTranslationFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;

public class CSEEntityByTranslationFilter extends CSEEntityFilterNode implements ICSEEntityByTranslationFilter {
  
  private ICSECouplingSource    filteredObject;
  private final Set<ICSEObject> translations = new HashSet<>();
  
  public CSEEntityByTranslationFilter()
  {
    super(FilterNodeType.byTranslation);
  }
  
  @Override
  public ICSECouplingSource getObject()
  {
    return filteredObject;
  }
  
  @Override
  public Collection<ICSEObject> getTranslations()
  {
    return translations;
  }
  
  void addTranslation(ICSEObject translation)
  {
    translations.add(translation);
  }
  
  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException
  {
    filteredObject = source;
  }
  
  @Override
  public Collection<ICSEEntityFilterNode> getSimpleFilters()
  {
    return Arrays.asList(new ICSEEntityFilterNode[] { this });
  }
  
  @Override
  public Collection<String> getIncludingClassifiers() throws CSFormatException
  {
    return new HashSet<>(); // Empty
  }
  
  @Override
  public Collection<String> getExcludedClassifiers() throws CSFormatException
  {
    return new HashSet<>(); // Empty
  }
  
  @Override
  public Collection<String> getContainedProperties() throws CSFormatException
  {
    return new HashSet<>(); // Empty
  }
  
  @Override
  public IJSONContent toJSON() throws CSFormatException
  {
    IJSONContent json = super.toJSON();
    json.setField("entity", filteredObject.toString());
    json.setField("op", " belongsto");
    JSONArray translationsJSON = new JSONArray();
    translations.forEach((ICSEObject translation) -> {
      translationsJSON.add(translation.toString());
    });
    json.setField("translations", translationsJSON);
    return json;
  }
  
  @Override
  public String toString()
  {
    StringBuffer translationStr = new StringBuffer();
    translations.forEach((ICSEObject translation) -> {
      translationStr.append(translation.toString()).append(" | ");
    });
    if (!translations.isEmpty()) {
      translationStr.setLength(translationStr.length() - 3);
    }
    return String.format("%s %s has %s %s", hasNot() ? "not (" : "", filteredObject.toString(), translationStr, hasNot() ? ")" : "");
  }
  
}
