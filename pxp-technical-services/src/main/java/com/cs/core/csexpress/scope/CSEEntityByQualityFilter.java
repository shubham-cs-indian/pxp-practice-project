package com.cs.core.csexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityByQualityFilter;
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
public class CSEEntityByQualityFilter extends CSEEntityFilterNode implements ICSEEntityByQualityFilter {

  private ICSECouplingSource filteredObject;
  private final Set<QualityFlag> flags = new HashSet<>();

  CSEEntityByQualityFilter() {
    super(FilterNodeType.byQuality);
  }

  @Override
  public ICSECouplingSource getObject() {
    return filteredObject;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    filteredObject = source;
  }

  void addFlag(QualityFlag flag) {
    flags.add(flag);
  }

  @Override
  public Collection<QualityFlag> getFlags() {
    return flags;
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
    JSONArray flagsJSON = new JSONArray();
    flags.forEach(
            (QualityFlag flag) -> {
              flagsJSON.add(flag.toString());
            });
    json.setField("flags", flagsJSON);
    return json;
  }

  @Override
  public String toString() {
    StringBuffer flagsStr = new StringBuffer();
    flags.forEach(
            (QualityFlag flag) -> {
              flagsStr.append(flag.toString()).append(" | ");
            });
    if (!flags.isEmpty()) {
      flagsStr.setLength(flagsStr.length() - 3);
    }
    return String.format(
            "%s %s.quality = %s %s",
            hasNot() ? "not (" : "",
            filteredObject.toString(),
            flagsStr,
            hasNot() ? ")" : "");
  }

}
