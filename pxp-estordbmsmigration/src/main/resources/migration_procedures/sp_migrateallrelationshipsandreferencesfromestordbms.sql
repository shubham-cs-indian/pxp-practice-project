-- PROCEDURE: staging.sp_migrateallrelationshipsandreferencesfromestordbms()

-- DROP PROCEDURE staging.sp_migrateallrelationshipsandreferencesfromestordbms();

CREATE OR REPLACE PROCEDURE staging.sp_migrateallrelationshipsandreferencesfromestordbms()
LANGUAGE 'plpgsql'

AS $BODY$
declare
begin
	
	-- Fill relations	
	INSERT INTO pxp.relation(
	propertyiid, side1entityiid, side2entityiid, 
	side1contextualobjectiid, side2contextualobjectiid)
	Select DISTINCT 
	-- propertyiid
	helper_relationshipconfig.iid propertyiid, 
	-- side1instanceiid
	baseentity.baseentityiid side1instanceid,
	-- side2instanceiid
	baseentity1.baseentityiid side2instanceid,
	-- side1contextiid
	CASE WHEN side1contextiid = 0 THEN NULL ELSE side1contextiid END,
	-- side2contextiid
	CASE WHEN side2contextiid = 0 THEN NULL ELSE side2contextiid END
	from (
		Select temp1.relationshipid, temp1.side1instanceid, temp1.side2instanceid, temp1.side1contextiid,
		 temp2.side2contextiid, temp1.commonrelationshipinstanceid, temp2.commonrelationshipinstanceid from
			(Select relationshipid, side1instanceid, side2instanceid, contextiid side1contextiid,
			 commonrelationshipinstanceid from staging.relationships temp_relationship2 JOIN
			staging.helper_relationshipconfig temp_relationship_config2 USING(relationshipid)
			where temp_relationship2.sideid = temp_relationship_config2.side1elementid) temp1,
			(Select contextiid side2contextiid, commonrelationshipinstanceid from staging.relationships temp_relationship1 
			JOIN staging.helper_relationshipconfig temp_relationship_config1 USING(relationshipid)
			where temp_relationship1.sideid = temp_relationship_config1.side2elementid) temp2
		 where temp1.commonrelationshipinstanceid = temp2.commonrelationshipinstanceid
	group by temp1.relationshipid, temp1.side1instanceid, temp1.side2instanceid, temp1.side1contextiid,
		 temp2.side2contextiid, temp1.commonrelationshipinstanceid, temp2.commonrelationshipinstanceid ) relations
	LEFT JOIN staging.helper_relationshipconfig helper_relationshipconfig 
		ON helper_relationshipconfig.relationshipid = relations.relationshipid
	LEFT JOIN staging.temp_baseentity baseentity ON baseentity.id = relations.side1instanceid
	LEFT JOIN staging.temp_baseentity baseentity1 ON baseentity1.id = relations.side2instanceid
	WHERE baseentity.baseentityiid IS NOT NULL
	AND baseentity1.baseentityiid IS NOT NULL
	AND helper_relationshipconfig.iid IS NOT NULL;
	
	-- Fill References
	INSERT INTO pxp.relation(
		propertyiid, side1entityiid, side2entityiid, 
		side1contextualobjectiid, side2contextualobjectiid)
	Select distinct tt.*, side1, side2
	from (
			Select 
			-- propertyiid
			(Select iid from staging.helper_relationshipconfig temp_relationship_config 
			 Where relationshipid = ref.referenceid) propertyiid, 
			-- side1entityiid
			(Select baseentityiid from staging.baseentity innertemp
			 where innertemp.id = ref.side1instanceid LIMIT 1) side1, 
			-- side2entityiid
			(Select baseentityiid from staging.baseentity innertemp
			 where innertemp.id = ref.side2instanceid LIMIT 1) side2
			from staging."references" ref
		) as tt
	where 
	tt.propertyiid IS NOT NULL
	AND tt.side1 IS NOT NULL
	AND tt.side2 IS NOT NULL
	AND tt.propertyiid NOT IN(Select propertyiid From pxp.relation Where side1entityiid = tt.side1 AND side2entityiid = tt.side2);
end
$BODY$;
