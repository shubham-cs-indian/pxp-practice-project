-- Create Indices For without loop approach

CREATE INDEX idx_staging_id
ON staging.baseentity(id);

CREATE INDEX idx_staging_parentid
ON staging.baseentity(parentid);

CREATE INDEX idx_staging_relationshipid
ON staging.relationships(relationshipid);

CREATE INDEX idx_helper_relationshipid
ON staging.helper_relationshipconfig(relationshipid);

CREATE INDEX idx_staging_attributeid
ON staging.attributeconflictingvalues(attributeid);

CREATE INDEX idx_staging_klassinstanceid
ON staging.attributeconflictingvalues(klassinstanceid);

CREATE INDEX idx_staging_language
ON staging.attributeconflictingvalues(language);

Create INDEX idx_staging_tagid
ON staging.tagrecord(id);

CREATE INDEX idx_staging_tag_conflict_tagid
ON staging.tagconflictingvalues(tagid);

CREATE INDEX idx_staging_tag_conflict_klassinstanceid
ON staging.tagconflictingvalues(klassinstanceid);

CREATE INDEX idx_staging_tag_conflict_tagvalues
ON staging.tagconflictingvalues(tagvalues);