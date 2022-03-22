package com.cs.core.rdbms.entity.idto;

import java.util.Set;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * A tags record is a property record made of: - tags
 *
 * @author Niraj
 */
public interface ITagsContentDTO extends ISimpleDTO {

  /**
   * @return the set of tags involved by the value
   */
  public Set<ITagDTO> getTags();

  /**
   * @param tags tag values to be set for a particular content.
   */
  public void setTags(ITagDTO... tags);

  /**
   * @param tagValueCode the sought tag value code
   * @return a tag found by value ID in the current tags or null
   */
  public ITagDTO getTagByCode(String tagValueCode);

  /**
   * Merge tags by TagValueID
   * @param tags modified tags that should be merged with the existing ones
   * @return tags value is changed or not
   */
  public boolean mergeTags(ITagDTO... tags);
  
}
