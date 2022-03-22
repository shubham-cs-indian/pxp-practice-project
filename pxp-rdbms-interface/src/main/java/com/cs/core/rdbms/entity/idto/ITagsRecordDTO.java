package com.cs.core.rdbms.entity.idto;

import java.util.Set;

/**
 * A tags record is a property record made of: - tags
 *
 * @author vallee
 */
public interface ITagsRecordDTO extends IPropertyRecordDTO, Comparable {

  /**
   * @return the set of tags involved by the value
   */
  public Set<ITagDTO> getTags();

  /**
   * @param tags overwritten tags
   */
  public void setTags(ITagDTO... tags);

  /**
   * @param tagValueCode the sought tag value code
   * @return a tag found by value ID in the current tags or null
   */
  public ITagDTO getTagByCode(String tagValueCode);

  /**
   * Merge tags by TagValueID
   *
   * @param tags modified tags that should be merged with the existing ones
   */
  public void mergeTags(ITagDTO... tags);
}
