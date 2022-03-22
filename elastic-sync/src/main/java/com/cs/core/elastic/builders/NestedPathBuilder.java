package com.cs.core.elastic.builders;

import com.cs.core.elastic.ibuilders.ISearchBuilder.Prefix;
import com.cs.core.elastic.ibuilders.ISearchBuilder.FieldType;

import static com.cs.core.elastic.builders.SearchBuilder.Fields;

public class NestedPathBuilder {

  protected static final String Separator = ".";
  StringBuilder path;

  public NestedPathBuilder(String initialPath)
  {
    path = new StringBuilder(initialPath);
  }

  public NestedPathBuilder append(String path)
  {
    this.path.append(Separator).append(path);
    return this;
  }

  public String path()
  {
    return path.toString();
  }

  /**
   *
   * @return nestedPathBuilder for attribute propertyObject.
   */
  public static NestedPathBuilder getPathForAttribute()
  {
    NestedPathBuilder nestedPathBuilder = new NestedPathBuilder(Fields.propertyObjects.name());
    nestedPathBuilder.append(Fields.attribute.name());
    return nestedPathBuilder;
  }

  /**
   * @param tagId Tag Id for tag on which filter needs to be applied.
   * @return Nested Path for Tag Property Object.
   */
  public static String getPathForTag(String tagId)
  {
    NestedPathBuilder nestedPathBuilder = new NestedPathBuilder(Fields.propertyObjects.name());
    nestedPathBuilder.append(Fields.tag.name());
    nestedPathBuilder.append(tagId);
    return nestedPathBuilder.path();
  }

  /**
   *
   * @param attributeCode attribute code for property to filter.
   * @param isNumeric is attribute numeric or not.
   * @return NestedPathBuilder Path for nested attribute object for search.
   */
  public NestedPathBuilder appendAttributeIdForSearch(String attributeCode, Boolean isNumeric)
  {
    Prefix prefix = isNumeric ? Prefix.i_ : Prefix.t_;
    append(prefix + attributeCode);
    if (isNumeric)
      append(FieldType.raw.name());
    return this;
  }

  /**
   *
   * @param attributeCode attribute code for property to filter.
   * @param isNumeric Is attribute numeric or not.
   * @return NestedPathBuilder Path for nested attribute object for exact check.
   */
  public NestedPathBuilder appendAttributeIdForExact(String attributeCode, Boolean isNumeric)
  {
    Prefix prefix = isNumeric ? Prefix.i_ : Prefix.t_;
    append(prefix + attributeCode);
    if (!isNumeric)
      append(FieldType.raw.name());
    return this;
  }

  public NestedPathBuilder appendAttributeIdForWildcard(String attributeCode, Boolean isNumeric)
  {
    Prefix prefix = isNumeric ? Prefix.i_ : Prefix.t_;
    append(prefix + attributeCode);
    if (isNumeric)
      append(FieldType.raw.name());
    else
      append(FieldType.rawlowercase.name());
    return this;
  }
}
