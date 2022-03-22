-- For baseType
INSERT INTO staging.helper_basetype(basetype, basetypevsenumvalue)
	VALUES ('com.cs.runtime.interactor.entity.AssetInstance', 4), 
	('com.cs.runtime.interactor.entity.textassetinstance.TextAssetInstance', 5),
	('com.cs.runtime.interactor.entity.MarketInstance', 11),
	('com.cs.runtime.interactor.entity.ArticleInstance', 2),
	('com.cs.runtime.interactor.entity.virtualcataloginstance.VirtualCatalogInstance', 8),
	('com.cs.runtime.interactor.entity.supplierinstance.SupplierInstance', 9);
	
INSERT INTO staging.successfulbatchcount VALUES ('cs',0,0,0,0);
INSERT INTO staging.successfulbatchcount VALUES ('csarchive',0,0,0,0);

INSERT INTO staging.helper_endpointconfig VALUES ('-1', null);