/**
 * Author:  vallee
 * Created: Mar 25, 2019
 */
#include "./include/sql-defs.sql"
#include "./include/sql-partitions.sql"
#include "./include/enum-constants.sql"

/*----------------------------------------------------------------------------*/
-- Delete base entity object 
/*----------------------------------------------------------------------------*/
_PROCEDURE(pxp.sp_deleteBaseEntityByIID)( p_baseEntityIID _IID)
_IMPLEMENT_AS
    vContextualObjectIID _IID;
begin
    -- update base entity where any of this entity is default image
    update pxp.baseEntity set defaultImageIID = null where defaultImageIID = p_baseEntityIID;

    -- update base entity where any of this entity is originBaseEntityIID
    update pxp.baseEntity set originBaseEntityIID = null where originBaseEntityIID = p_baseEntityIID;

    -- delete from classifer links
    delete from pxp.baseEntityClassifierLink where baseEntityIID = p_baseEntityIID;
    
    -- delete from contextual base entity link
    delete from pxp.contextBaseEntityLink where baseEntityIID = p_baseEntityIID;

    -- delete from collection base entity link
    delete from pxp.collectionbaseentitylink where baseEntityIID = p_baseEntityIID;
    
     -- delete from base entity  taxonomy conflict link
    delete from pxp.baseentitytaxonomyconflictlink where entityIID = p_baseEntityIID OR sourceEntityIID = p_baseEntityIID;
    
   -- delete from base entity locale id link(Delete Language Translation)
    delete from pxp.baseentitylocaleidlink where baseEntityIID = p_baseEntityIID;

    delete from pxp.baseentityqualityrulelink where baseEntityIID = p_baseEntityIID;
    
    select contextualobjectiid into vContextualObjectIID from pxp.baseentity where baseEntityIID = p_baseEntityIID ;

    -- eventually delete from base entity table
    delete from pxp.baseEntity where baseEntityIID = p_baseEntityIID ;

    if ( vContextualObjectIID is not null ) then
        delete from pxp.contextBaseEntityLink where contextualobjectiid = vContextualObjectIID;

        delete from pxp.contextualobject where contextualobjectiid = vContextualObjectIID;
    end if;

    delete from pxp.uniquenessviolation uv where (uv.sourceiid = p_baseEntityIID OR uv.targetiid = p_baseEntityIID);

    delete from pxp.relationcouplerecord where sourceentityid = p_baseEntityIID OR targetentityid = p_baseEntityIID;
end
_IMPLEMENT_END;

/*----------------------------------------------------------------------------*/
-- Delete base entity objects in bulk
/*----------------------------------------------------------------------------*/
_PROCEDURE( pxp.sp_deleteBaseEntityByIIDs )( p_baseEntityIIDs _IIDARRAY)
_IMPLEMENT_AS
    vEntityIID       _IID;
    vEntityIIDIdx    _INT;
begin
    for vEntityIIDIdx IN 1 .. _COUNT( p_baseEntityIIDs) loop
        vEntityIID := _ARRAY( p_baseEntityIIDs, vEntityIIDIdx);
        _CALL pxp.sp_deleteBaseEntityByIID( vEntityIID);
    end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- ADD CLASSIFIERs
---------------------------------------------------------------------------------
_PROCEDURE (pxp.sp_addClassifiers)(p_baseEntityIID _IID, p_ClassifierIIDs _IIDARRAY)
_IMPLEMENT_AS
    vClassifierIID     _IID;
    vClassifierIdx    _INT;
begin
    for vClassifierIdx in 1 .. _COUNT( p_ClassifierIIDs ) loop
        /* insert the new baseentityclassifierlink */
		vClassifierIID := _ARRAY( p_ClassifierIIDs, vClassifierIdx);
        insert into pxp.BaseEntityClassifierLink( baseentityiid, otherclassifieriid)
            values ( p_baseEntityIID, vClassifierIID);  
    end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a regular base entity with a predefined IID (not an extension)
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_createBaseEntity ) ( p_baseentityiid _IID, p_baseentityid _STRING, p_contextCode _STRING, p_catalogCode _STRING, 
                p_organizationCode _STRING,  p_classifieriid _IID, p_basetype _INT, p_childLevel _INT, p_sourceCatCode _STRING,
                p_localeID _STRING, p_parentIID _IID, p_topParentIID _IID, p_defaultimageiid _IID, p_sourceBeIID _IID, p_hashcode _STRING, 
				p_extension _JSON, p_createTime _LONG, p_createUserIID _IID, p_isClone _BOOLEAN, p_endpointCode _STRING, p_sourceOrganizationCode _STRING)
_IMPLEMENT_AS
    vContextualObjectIID _IID;
	vTopParentIID _LONG;
	vChildLevel _INT;
begin
	vTopParentIID := p_topParentIID;
	vChildLevel := p_childLevel;
	if ( p_parentIID is not null ) then /* manage consistency of parent to child information */
		begin
			select a.topParentIID, a.childLevel+1 into vTopParentIID, vChildLevel
			from pxp.BaseEntity a 
			where a.baseentityIID = p_parentIID;
			if ( vTopParentIID is null ) then
				vTopParentIID := p_topParentIID;
			end if;
		exception
			when no_data_found then _RAISE( 'Unknown parent IID: %', _TO_VARCHAR(p_parentIID));
		end;
	end if;
    if ( p_contextCode is not null ) then /* create an enty in contextual object with same IID */
        insert into pxp.contextualObject ( contextualObjectIID, contextCode )
        values ( p_baseentityiid, p_contextCode );
        vContextualObjectIID := p_baseentityiid;
    else
        vContextualObjectIID := null;
    end if;
    insert into pxp.baseentity 
        ( baseentityiid, baseentityid, catalogcode, organizationCode, contextualObjectIID, classifieriid, 
            basetype, childLevel, sourcecatalogcode, baselocaleid, parentIID, topParentIID, hashcode, defaultimageiid, originBaseEntityIID, entityExtension, 
            createUserIID, lastModifiedUserIID, createTime, lastModifiedTime, isclone, endpointCode, sourceOrganizationCode)
    values( p_baseentityiid, p_baseentityid, p_catalogCode, p_organizationCode, vContextualObjectIID, p_classifieriid, 
            p_basetype, vChildLevel, p_sourceCatCode, p_localeID, p_parentIID, vTopParentIID, p_hashcode, p_defaultimageiid, p_sourceBeIID, p_extension, 
			p_createUserIID, p_createUserIID, p_createTime, p_createTime, p_isClone, p_endpointCode, p_sourceOrganizationCode);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create a regular base entity (not an extension)
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_createBaseEntity ) (p_baseentityiid _IID, p_baseentityid _STRING, p_contextCode _STRING, p_catalogCode _STRING, p_organizationCode _STRING, 
                p_classifieriid _IID, p_basetype _INT, p_childLevel _INT, p_sourceCatCode _STRING,
                p_localeID _STRING, p_parentIID _IID, p_topParentIID _IID, p_defaultimageiid _IID, p_sourceBeIID _IID, p_hashcode _STRING, 
				p_extension _JSON, p_createTime _LONG, p_createUserIID _IID, p_isClone _BOOLEAN, p_endpointCode _STRING, p_sourceOrganizationCode _STRING)
_RETURN _IID
_IMPLEMENT_AS
begin
	if(p_baseentityiid = 0 ) then
    	p_baseentityiid := _NEXTVAL( pxp.seqIID);
    end if;
	_CALL pxp.sp_createBaseEntity( p_baseentityiid, p_baseentityid, p_contextCode, p_catalogCode, p_organizationCode,
                p_classifieriid, p_basetype, p_childLevel, p_sourceCatCode,
                p_localeID, p_parentIID, p_topParentIID, p_defaultimageiid, p_sourceBeIID, p_hashcode, 
				p_extension, p_createTime, p_createUserIID, p_isClone, p_endpointCode, p_sourceOrganizationCode);
    return p_baseentityiid;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Linked Collection to baseEntityIIDs
---------------------------------------------------------------------------------

_PROCEDURE (pxp.sp_updatelinkedbaseentityforcollection) (
	p_collectioniid _IID,
	p_linkedbaseentityiids _IIDARRAY)

_IMPLEMENT_AS
   vLinkedBaseEntityIIDIdx _INT;
   vLinkedBaseEntityIID _INT;
begin
    
	for vLinkedBaseEntityIIDIdx IN 1 .. coalesce( array_upper(p_linkedbaseentityiids, 1), 0)loop
		vLinkedBaseEntityIID := p_linkedbaseentityiids[vLinkedBaseEntityIIDIdx];
		insert into pxp.collectionbaseentitylink VALUES (p_collectioniid, vLinkedBaseEntityIID);
	end loop;
		
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Create collection
---------------------------------------------------------------------------------

_FUNCTION (pxp.fn_createCollection) (
	p_collectionType _INT, p_collectionCode _STRING, p_parentIID _IID, p_createTime _LONG, p_createUserIID _IID, 
	p_searchCriteria _JSON, p_isPublic _BOOLEAN, p_catalogcode _STRING, 
	p_organizationcode _STRING, p_linkedBaseEntityIIDs _IIDARRAY)
_RETURN  _IID

_IMPLEMENT_AS
	vCollectionIID _IID;
begin

	if(vCollectionIID is null OR vCollectionIID = 0) THEN
   		vCollectionIID := _NEXTVAL(pxp.seqIID);
   	END IF;
	
	INSERT INTO pxp.collection(collectioniid, collectiontype, collectioncode, parentiid, createuseriid, lastmodifieduseriid, createtime, 
	lastmodifiedtime, searchcriteria, ispublic, catalogcode, organizationcode)
	VALUES (vCollectionIID, p_collectionType, p_collectionCode, p_parentIID, p_createUserIID, p_createUserIID, p_createTime, p_createTime, 
	p_searchCriteria, p_isPublic, p_catalogcode, p_organizationcode);
				
   	if(p_collectionType = 1) THEN
		IF(p_linkedBaseEntityIIDs is not null) THEN
			_CALL pxp.sp_updatelinkedbaseentityforcollection(vCollectionIID, p_linkedBaseEntityIIDs);
		END IF;
	END IF;
	
	return vCollectionIID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Remove Linked Base Entity from collectionbaseentitylink
---------------------------------------------------------------------------------
_PROCEDURE (pxp.fn_removeLinkedBaseEntities)(
	p_collectioniid _IID,
	p_removelinkedbaseentityiids _IIDARRAY)
_IMPLEMENT_AS
  v_removeBaseEnitiyIID _IID;
begin
    for p_removelinkedbaseentityiidx IN 1 .. coalesce( array_upper(p_removelinkedbaseentityiids, 1), 0)loop
 	v_removeBaseEnitiyIID := p_removelinkedbaseentityiids[p_removelinkedbaseentityiidx];
	delete from pxp.collectionbaseentitylink where baseentityiid = v_removeBaseEnitiyIID AND collectioniid = P_collectioniid;
 	end loop;
end
_IMPLEMENT_END;
--------------------------------------------------------------------------------------------------
-- Add Linked Base Entity from collectionbaseentitylink (if already added v_count is set to TRUE)
--------------------------------------------------------------------------------------------------
_FUNCTION (pxp.fn_addLinkedBaseEntities)(p_collectioniid _IID,p_addedlinkedbaseentityiids _IIDARRAY)
	_RETURN _INT
_IMPLEMENT_AS
  v_count _INT := 0;
  vaddedBaseEntityIID _IID;
begin
	for p_addedlinkedbaseentityiidx IN 1 .. coalesce( array_upper(p_addedlinkedbaseentityiids, 1), 0)loop
 	begin
	vaddedBaseEntityIID := p_addedlinkedbaseentityiids[p_addedlinkedbaseentityiidx];
  	insert into pxp.collectionbaseentitylink VALUES (p_collectioniid, vaddedBaseEntityIID);
	exception WHEN unique_violation THEN 
      v_count := 1;
    end;
 	end loop;
  return v_count;	
end
_IMPLEMENT_END;
------------------------------------------------------------------------------------------------------------------------------------------
-- Updating collection (returns 0 if no exception and 1 if exception i.e. addedlinkedbaseentityiids are already present in collection)
------------------------------------------------------------------------------------------------------------------------------------------

_FUNCTION (pxp.fn_updateCollection) (
	P_collectioniid _IID,
	p_collectioncode _STRING,
	p_modifiedTime _LONG, 
	p_modifiedUserIID _IID, 
	p_searchCriteria _JSON,
	p_ispublic _BOOLEAN,
	p_addedlinkedbaseentityiids _IIDARRAY,
	p_removelinkedbaseentityiids _IIDARRAY)
	_RETURN _INT

_IMPLEMENT_AS
	v_result _INT := 0;
begin
 	UPDATE pxp.collection  SET  collectioncode = p_collectioncode, searchcriteria = p_searchCriteria, ispublic = p_ispublic, lastmodifieduseriid = p_modifiedUserIID, lastmodifiedtime = p_modifiedTime
	WHERE collection.collectioniid = P_collectioniid;
		
	if(p_addedlinkedbaseentityiids is not null)THEN
		v_result := pxp.fn_addLinkedBaseEntities(p_collectioniid, p_addedlinkedbaseentityiids);
	END IF;
	
	if(p_removelinkedbaseentityiids is not null)THEN
		_CALL pxp.fn_removeLinkedBaseEntities(p_collectioniid, p_removelinkedbaseentityiids);
	END IF;
	
 return v_result;	
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Clone a contextual object
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_CloneContextualObject ) ( p_contextualObjectIID _IID, p_newContextualObjectIID _IID )
_IMPLEMENT_AS
begin
    insert into pxp.ContextualObject 
        ( select p_newContextualObjectIID, contextCode, cxttimerange, cxtTags
            from pxp.ContextualObject 
            where contextualObjectIID = p_contextualObjectIID );
end
_IMPLEMENT_END;
-- function version
_FUNCTION( pxp.fn_CloneContextualObject) ( p_contextualObjectIID _IID)
_RETURN _IID
_IMPLEMENT_AS
	vContextualObjectIID _IID;
begin
	if ( p_contextualObjectIID is null ) then
		return null;
	end if;
	vContextualObjectIID := _NEXTVAL( pxp.seqIID);
	_CALL pxp.sp_CloneContextualObject( p_contextualObjectIID, vContextualObjectIID);
	return vContextualObjectIID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Clone a contextual object and link to newly created contextual baseEntity
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_CloneContextualObject) ( p_newContextualBaseEntityIID _IID, p_contextualObjectIID _IID)
_RETURN _IID
_IMPLEMENT_AS
	vContextualObjectIID _IID;
begin
	if ( p_contextualObjectIID is null ) then
		return null;
	end if;
	vContextualObjectIID := _NEXTVAL( pxp.seqIID);
	_CALL pxp.sp_CloneContextualObject( p_contextualObjectIID, vContextualObjectIID);
	update pxp.baseentity set contextualobjectiid = vContextualObjectIID where baseentity.baseentityiid = p_newContextualBaseEntityIID;
	return vContextualObjectIID;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- update linked base entities with context 
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateLinkedBaseEntityForContext ) ( p_contexualObjectIID _IID, p_linkedBaseEntityIIDs _IIDARRAY)
_IMPLEMENT_AS
   vLinkedBaseEntityIIDIdx    _INT;
   vLinkedBaseEntityIID       _IID;
begin
    delete from pxp.contextBaseEntityLink where contextualobjectiid = p_contexualObjectIID;
    for vLinkedBaseEntityIIDIdx IN 1 .. _COUNT( p_linkedBaseEntityIIDs) loop
        vLinkedBaseEntityIID := _ARRAY( p_linkedBaseEntityIIDs, vLinkedBaseEntityIIDIdx);
        insert into pxp.contextBaseEntityLink VALUES (p_contexualObjectIID, vLinkedBaseEntityIID);
    end loop;
end
_IMPLEMENT_END;


---------------------------------------------------------------------------------
-- Update contextual information of a base entity
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateBaseEntityContextualData )( p_ObjectIID _IID,
                p_startTime _LONG, p_endTime _LONG, p_cxtTags _STRING , p_linkedBaseEntityIIDs _IIDARRAY)
_IMPLEMENT_AS
v_cxttime int8range;
begin
	if(p_startTime = 0 and p_endTime = 0)
	then 
		v_cxttime = null;
	else
		v_cxttime = int8range(p_startTime, p_endTime, '[]');
	end if;
    update pxp.contextualobject 
        set cxttimerange = v_cxttime,
            cxtTags = p_cxtTags :: hstore
        where contextualObjectIID = p_ObjectIID;
    _CALL pxp.sp_updateLinkedBaseEntityForContext(p_ObjectIID, p_linkedBaseEntityIIDs);
    
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update contextual information of a property record
---------------------------------------------------------------------------------
_FUNCTION( pxp.sp_upsertRecordContextualData )( p_contextualObjectIID _IID, p_contextCode _STRING,
                p_startTime _LONG, p_endTime _LONG, p_cxtTags _STRING, p_linkedBaseEntityIIDs _IIDARRAY )

_RETURN _IID
_IMPLEMENT_AS

v_contextualObjectIID _IID ;
v_cxttime int8range;
begin
    v_contextualObjectIID := p_contextualObjectIID;
    if(p_startTime = 0 and p_endTime = 0)
	then 
		v_cxttime = null;
	else
		v_cxttime = int8range(p_startTime, p_endTime, '[]');
	end if;
    if (v_contextualObjectIID is null) then 
	 v_contextualObjectIID := _NEXTVAL( pxp.seqIID);
	 insert into pxp.contextualobject (contextualobjectiid, contextCode, cxttimerange , cxtTags) 
	 values (v_contextualObjectIID, p_contextCode, v_cxttime , p_cxtTags :: hstore);
     else 
        update pxp.contextualobject 
        set cxttimerange = v_cxttime, cxtTags = p_cxtTags :: hstore
        where contextualObjectIID = v_contextualObjectIID ;
     end if;
    _CALL pxp.sp_updateLinkedBaseEntityForContext(v_contextualObjectIID, p_linkedBaseEntityIIDs);
    return v_contextualObjectIID;
    
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update a base entity and its updatable information
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateBaseEntity ) ( p_baseentityiid _IID, p_defaultImageIID _IID, p_hashcode _STRING, p_extension _JSON,
	p_lastmodified _LONG, p_lastModifiedUserIID _IID)
_IMPLEMENT_AS
begin
    update pxp.baseentity set  defaultImageIID = p_defaultImageIID, hashcode = p_hashcode, 
       entityExtension = p_extension, lastModifiedTime = p_lastmodified, lastModifiedUserIID = p_lastModifiedUserIID
	   where baseentityIID = p_baseentityiid;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Update only the traking information of base entity
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_updateBaseEntityTracking ) ( p_baseentityiid _IID, p_lastmodified _LONG, p_lastModifiedUserIID _IID)
_IMPLEMENT_AS
begin
    update pxp.baseentity set lastModifiedTime = p_lastmodified, lastModifiedUserIID = p_lastModifiedUserIID
	   where baseentityIID = p_baseentityiid;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- Add children
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_addChildren )(p_embeddedType _INT, p_parentIID _IID, p_childrenIIDs _IIDARRAY)
_IMPLEMENT_AS
    vChildIID       _IID;
    vChildIIDIdx    _INT;
    vParentChildLevel _INT;
    vTopParentIID     _IID;
    vparentIID      _IID;
begin
    if ( p_childrenIIDs is null ) then
        return;
    end if;
    select childLevel, coalesce( topParentIID, baseEntityIID) into vParentChildLevel, vTopParentIID 
        from pxp.baseEntity where baseEntityIID = p_parentIID;
    for vChildIIDIdx IN 1 .. _COUNT( p_childrenIIDs) loop
        vChildIID := _ARRAY( p_childrenIIDs, vChildIIDIdx);
        -- TODO here: review the parent-children relation !!
        select parentIID into vparentIID from baseEntity where baseEntityIID = vChildIID;
            if(vparentIID is null) then
            	update pxp.baseEntity set childLevel = vParentChildLevel + 1, topParentIID =  vTopParentIID, 
            		parentIID = p_parentIID, embeddedType = p_embeddedType
                	where baseEntityIID = vChildIID;
           else
                _RAISE( 'Cannot modify parentIID once assigned in %', _TO_VARCHAR(vChildIID));
           end if;
    end loop;
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- remove children
---------------------------------------------------------------------------------
_PROCEDURE( pxp.sp_removeChildren )( p_parentIID _IID, p_childrenIIDs _IIDARRAY)
_IMPLEMENT_AS
begin
    /* removing the children object is enough */
    _CALL pxp.sp_deleteBaseEntityByIIDs( p_childrenIIDs);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- generate a new unique cloned entity ID
---------------------------------------------------------------------------------
_FUNCTION( pxp.fn_newClonedBaseEntityID )( p_baseentityIID _IID)
_RETURN _STRING
_IMPLEMENT_AS
	vRootID _VARCHAR;
begin
	/* retrieve the source of clone and the initial ID by recursive call */
	with _RECURSIVE originOfClone( baseentityIID, baseentityid, originbaseentityiid) as (
		select baseentityiid, baseentityid, originbaseentityiid from pxp.baseentity 
		where baseentityiid = p_baseentityiid
		union all
		select a.baseentityiid, a.baseentityid, a.originbaseentityiid from pxp.baseentity a 
		join originOfClone b on a.baseentityiid = b.originbaseentityiid
	) 
	select baseentityid into vRootID from originOfClone where originbaseentityiid is null; 
	/* return a unique cloned object ID from the root ID */
	return vRootID || '#' || _NEXTVAL( pxp.uniqueIID);
end
_IMPLEMENT_END;

---------------------------------------------------------------------------------
-- upsert Taxonomy Inheritance Conflict
---------------------------------------------------------------------------------

_PROCEDURE( pxp.sp_upsertTaxonomyInheritanceConflict )( p_entityIID _IID, p_sourceEntityIID _IID,
                p_propertyIID _IID, p_isResolved _BOOLEAN)
_IMPLEMENT_AS
v_entityIID _IID ;

begin
    begin
	select entityIID into v_entityIID from pxp.baseentitytaxonomyconflictlink where entityIID = p_entityIID;
	exception
        when no_data_found then v_entityIID := null;
    end;
	 
    if (v_entityIID is not null) then
        update pxp.baseentitytaxonomyconflictlink 
        set
        entityIID = p_entityIID,
        sourceEntityIID = p_sourceEntityIID,
        propertyIID = p_propertyIID,
        isResolved = p_isResolved 
       	where entityIID = p_entityIID;
    else
    	insert into pxp.baseentitytaxonomyconflictlink(entityIID, sourceEntityIID, propertyIID, isResolved) 
       values (p_entityIID, p_sourceEntityIID, p_propertyIID, p_isResolved);
    end if;
    
end
_IMPLEMENT_END;
  
---------------------------------------------------------------------------------
-- adding Language Translations to Cloned Entity.
---------------------------------------------------------------------------------

_PROCEDURE (pxp.sp_addLanguageTranslation)(p_baseEntityIID _IID, p_localeIds _VARARRAY)
_IMPLEMENT_AS
    vLocaleId     _VARCHAR;
    vLocaleIdIdx  _INT;
begin
    for vLocaleIdIdx in 1 .. _COUNT( p_localeIds ) loop
        /* inserting localeIds for clonedEntity in baseentitylocaleidlink */
		vLocaleId := _ARRAY( p_localeIds, vLocaleIdIdx);
        insert into pxp.BaseEntityLocaleIdLink( baseentityiid, localeid)
            values ( p_baseEntityIID, vLocaleId);  
    end loop;
    
end
_IMPLEMENT_END;

------------------------------------------------------------------------------------
---- setting default image to an article
------------------------------------------------------------------

_FUNCTION (pxp.fn_getNewDefaultImage) (p_baseEntityIID _IID, p_propertyIID _IID, p_baseType _INT)
_RETURN _IID
_IMPLEMENT_AS
    vDefaultImageIID _IID;
begin
	select r.side2EntityIID into vDefaultImageIID from pxp.relation r join pxp.baseentity be 
    ON be.baseentityiid = r.side2entityiid and be.hashcode is not null
     and r.side1EntityIID = p_baseEntityIID and r.propertyIID = p_propertyIID;
	if(vDefaultImageIID is null) then
	
		select baseEntityIID into vDefaultImageIID from pxp.baseEntity where hashcode is not null and baseType = p_baseType
		and (baseEntityIID in
		(select side2EntityIID from pxp.relation where side1EntityIID = p_baseEntityIID)
		OR baseEntityIID in
		(select side1EntityIID from pxp.relation where side2EntityIID = p_baseEntityIID));
		
	end if;
		
		return vDefaultImageIID;
		
end
_IMPLEMENT_END;


#ifdef PGSQL11
-- end of PGSQL Entity Procedure/Function declarations
#elif ORACLE12C
-- end of ORACLE Entity Procedure/Function declarations
#endif
