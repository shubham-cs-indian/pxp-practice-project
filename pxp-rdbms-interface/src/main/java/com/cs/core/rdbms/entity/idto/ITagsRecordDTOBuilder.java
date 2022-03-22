package com.cs.core.rdbms.entity.idto;

/**
 * The Builder interface to construct ITagsRecordDTO
 * 
 * @author janak
 */
public interface ITagsRecordDTOBuilder extends IPropertyRecordDTOBuilder<ITagsRecordDTOBuilder> {
  
  /**
   * @param tags overwritten tags
   * @return ITagsRecordDTOBuilder
   */
  public ITagsRecordDTOBuilder tags(ITagDTO... tags);

  /**
   * factory method to get ITagsRecordDTO
   *
   * @return ITagsRecordDTO
   */
  public ITagsRecordDTO build();

  
}
