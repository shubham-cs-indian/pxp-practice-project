# Perform following actions in the order mentioned below

## PREREQUISITE
1. pxp-rdbms-implementation 'gradle rdbms-renew' should be done.
2. Below servers need to be up:
   - orient 3.0.11 (Copy existing orient 2.2.22 data into orient 3.0.11, which needs to be migrated)
   - zookeeper 3.4.9
   - elasticsearch 2.1.2 (Change the port to other than 9200, which is mentioned in elastic.server.port key of migration-props/migation.properties)
   - elasticsearch 7.8.0
   - jetty server of pxp-estordbmsmigration (gradle jettyStart)
3. Copy \pxp-estordbmsmigration\migration-props folder into PXP_RDBMS_HOME(environment variable) location.
4. Create new Property manually mentioned below in orientDB studio:
   1) propertyIID with Type Long for Vertextype Attribute, Tag, Root_Relationship.
   2) classifierIID with Type Long for Vertextype RootKlass, Klass_Taxonomy.
   3) userIID with Type Long for Vertextype User.
5. Run Following queries:(optional- only for Lutzz data)
	a) update Normalization set valueAsHTML = "" where entityId like 'Ax_DAM_Texture_Material_%_Percent' and valueAsHTML = '<p></p>'
	b) update Normalization set entityId = "html1" where entityId = 'dd6f5501-1d18-429b-bda6-843ca34e734b'
	c) delete vertex from Process_Event where label__null = "Standard_Transfer_Workflow"
6. Update postgres.port and postgres.host in migation.properties file according to the system which has data to be migrated.
7. if archival data is needed, Delete below docs from Elastic(2.1.2) (optional- only for Lutzz data)
	csarchive/klassinstancecache/25c40059-22a3-43cf-b793-230ee48e6b89_pim_ORG_SUP_6593
	csarchive/klassinstancecache/991e3c8a-1a56-4b46-8626-47c310653e88_pim_-1
	csarchive/klassinstancecache/13866e8b-865d-49ab-a5c4-c19d5af7bb77_pim_-1
	csarchive/klassinstancecache/41dc237a-3175-4352-8abd-2d6ff24fd966_pim_-1
	csarchive/klassinstancecache/a16ad8b6-2894-4eb6-9984-b14c16ed2455_pim_-1
	csarchive/supplierinstancecache/ORG_SUP_3731
	RequestMethod: DELETE


## STEPS
1. Remove existing klasses data which are removed from PXP
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/MigrationForRemovedKlasses/cs/
   - RequestBody: {}
2. Change type and replace code with ID
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/MigrationForTypeChange/cs/
   - RequestBody: {}
3. Add isRoot property in Tags vertex
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/Orient_Migration_for_IsRoot_Field_In_Tag/cs/
   - RequestBody: {}
4. Sync ODB with RDBMS and IID to ODB
   - RequestMethod : POST
   - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationforsyncconfig
   - RequestBody: {}
5. Remove properties from logo Configuration
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/MigrationForLogoConfiguration/cs/
   - RequestBody: {}
6. To Migrate Endpoint
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/Orient_Endpoint_Migration/cs/
   - RequestBody: {}
7. To Migrate Mapping
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/Config_Mapping_Migration/cs/
   - RequestBody: {}
8. To Migrate Authorization
   - RequestMethod : POST
   - RequestURL: http://localhost:2480/Config_Authorization_Migration/cs/
   - RequestBody: {}
   
Run following query: 
	update tag set isFilterable = false where type = 'com.cs.core.config.interactor.entity.tag.Tag' and isRoot = false and isFilterable = true

	Update Role set entities=[] where code='adminrole'
   
9. To create and set-up staging schema, hit below URL:
   - RequestMethod : GET
   - RequestURL: http://localhost:8091/pxp-estordbmsmigration/createstagingschema
   This call will do following : 
   - Create a new schema with name 'staging'.
   - Execute src/main/resources/migration_procedures/helpertables_createscript.sql
   - Create all staging tables using sql create scripts from folder '/pxp-estordbmsmigration/src/main/resources/staging-schema'
   - Create required procedures from create sripts '/pxp-estordbmsmigration/src/main/resources/migration_procedures'
   - Execute src/main/resources/migration_procedures/helpertables_insertscript.sql
10. To get all data into the helper tables, hit below URL:
   - RequestMethod : POST
   - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationhelpertables/
   - RequestBody: {}
11. To get all data from cs index into the staging tables, hit below URL:
    - RequestMethod : POST
	- RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationfromestordbms/
	- RequestBody: { "shouldMigrateArchive": "false"}
12. To get all data from csarchive index into the staging tables, hit below URL:(Note: Make sure there is proper data in csarchive and this migration is needed)
    - RequestMethod : POST
	- RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationfromestordbms/
	- RequestBody: { "shouldMigrateArchive": "true"}

If archive migration is executed, run below query on PostgreSQL. (Optional for Lutzz data) 
	Delete from staging.tagrecord where tagvalues = '"Tx_Logistik_IncoTerms_DDP"=>"100"'
	
13. To execute workaround, hit below URL:
	- NOTE : This step is OPTIONAL. Workaround script written due to issues in Lutz data. Issue : lastmodified was null for some entities. User 'baf3033b-aeaa-4425-9ae5-618754d06b93' was not present in orient. As a workaround updated lastmodified & createdby as admin for such entities.
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationworkaround
14. To create indices & execute sp_migrateallentitiesfromestordbms procedure, hit below URL:
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationbaseentities
15. To execute sp_migrateallvaluerecordsfromestordbms procedure, hit below URL:
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationvaluerecords
16. To execute sp_migratealltagrecordsfromestordbms_without_loop procedure, hit below URL:
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationtags
17. To create execute sp_migrateallrelationshipsandreferencesfromestordbms procedure, hit below URL:
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationrelationships
18. To create execute sp_migratetasksfromestordbms procedure, hit below URL:
	- RequestMethod : GET
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationtasks
19. To create versions(Revisions) data hit below URL:
    Run this step after all runtime data will be created.
    - RequestMethod : GET
    - RequestURL : http://localhost:8091/pxp-estordbmsmigration/migraterevisions
	
20. To remove cid from ODB run below mentioned query on ODB console.
    - update v remove cid.
	
21. For property collection position
    - RequestMethod : POST
    - RequestURL : http://localhost:2480/Config_Property_Collection_Repair_Position_Sequence_Migration/cs/
    - RequestBody: {}
   
22. Do DataInitialization to create indexes in elasticsearch 7.8.0 and also will create newly added vertexTypes:
	- RequestMethod : POST
    - RequestURL: http://localhost:8092/pxp-ui/datainitialization
    - RequestBody: {}

23. create default instance of klass/taxonomy for classification data transfer.
   - RequestMethod : POST
   - RequestURL: http://localhost:8091/pxp-estordbmsmigration/migrationforcreatedefaultinstance
   - RequestBody: {}	
	
24. To Migrate and evaluate DQ Rules, KPI Rules, Must-Should and Product Identifier to generate violations freshly:
	- RequestMethod : POST
	- RequestURL : http://localhost:8092/pxp-ui/migratecontentkpi
	- RequestBody : { "ids" : ["id1","id2"....."idn"] } 
	- Note: The migration will generate violations for the contents mentioned in ids parameter, if not provided, will evaluate for all the contents present in the system.
25. To Migrate Bookmark and Collection:
	- RequestMethod : GET
	- RequestURL : http://localhost:8091/pxp-estordbmsmigration/migratebookmarkandcollectionfromestordbms
	- RequestBody: {}
26. To generate searchable documents, hit below URL: (Note: Login into application for this migration) 
	- RequestMethod : PUT
    - RequestURL: http://localhost:8092/pxp-ui/runtime/regeneratesearchabledocs?lang=en_US&organizationId=-1&physicalCatalogId=pim&portalId=pim&endpointId=&endpointType=&dataLanguage=en_US
    - RequestBody: { "baseEntityIIDs":[ ], "classifierCodes":[ ]}
27. TO Migrate Archived Instances and update its searchable:(Note: Required only when archive migration is needed)
	- RequestMethod : POST
    - RequestURL: http://localhost:8091/pxp-estordbmsmigration/archivedmigrationfromestordbms
    - RequestBody: {}
28. To Migrate Audit Log Information Data:
	- RequestMethod : GET
	- RequestURL : http://localhost:8091/pxp-estordbmsmigration/migrateAuditLogInformation
	- RequestBody: {}
29. To Remove deprecated fields (From extensions, events & references) from OrientDB nodes.
	- RequestMethod : POST
	- RequestURL : http://localhost:2480/RemoveDeprecatedFields/cs/
	- RequestBody: {}