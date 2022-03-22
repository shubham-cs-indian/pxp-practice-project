package com.cs.core.rdbms.entity.idto;

import java.util.Set;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct ICollectionDTO
 * 
 * @author Harsh
 */
public interface ICollectionDTOBuilder extends IRootDTOBuilder<ICollectionDTO> {
	
	/**
	 * @param parentIID, the IID of parent collection if exists
	 * @return ICollectionDTOBuilder
	 */
	public ICollectionDTOBuilder parentIID(long parentIID);

	/**
	 * @param searchCriteria, json having search and filter related info for Bookmarks
	 * @return ICollectionDTOBuilder
	 * @throws CSFormatException in case of format error
	 */
	public ICollectionDTOBuilder searchCriteria(IJSONContent searchCriteria) throws CSFormatException;

	
	/**
	 * @param isPublic,  whether Collection/Bookmark is public or private
	 * @return ICollectionDTOBuilder
	 */
	public ICollectionDTOBuilder isPublic(boolean isPublic);

	/**
	 * @param linkedBaseEntityIIDs, the IID of the base entities linked to this
	 *         Collection, clear all existing linkedBaseEntityIIDs
	 * @return ICollectionDTOBuilder
	 */
	public ICollectionDTOBuilder linkedBaseEntityIIDs(Set<Long> linkedBaseEntityIIDs);

	/**
	 * Add baseEntity IID to existing linkedBaseEntityIIDs.
	 * @param linkedBaseEntityIID, the IID of the base entity linked to this Collection
	 * @return ICollectionDTOBuilder
	 */
	public ICollectionDTOBuilder linkedBaseEntityIID(Long linkedBaseEntityIID);
  
	/**
	 * factory method to construct ICollectionDTO
	 * @return ICollectionDTO
	 */
	@Override
	public ICollectionDTO build();
	
}
