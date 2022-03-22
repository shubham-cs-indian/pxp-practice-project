
Update staging.baseentity set lastmodifiedby = 'admin' where lastmodifiedby is null;

Update staging.baseentity set lastmodifiedby = 'admin'  where baseentity.lastmodifiedby = 'baf3033b-aeaa-4425-9ae5-618754d06b93';

Update staging.baseentity set createdby = 'admin' where baseentity.createdby = 'baf3033b-aeaa-4425-9ae5-618754d06b93';
