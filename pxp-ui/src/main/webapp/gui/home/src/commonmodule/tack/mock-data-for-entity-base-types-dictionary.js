/**
 * Created by CS56 on 3/8/2016.
 */

/**
 * @alias contentBaseType:  "KlassInstance"
 * @alias attributeInstanceBaseType:  "AttributeInstance"
 * @alias imageAttributeInstanceBaseType:  "ImageAttributeInstance"
 *
 */

export default {
  contentBaseType: "com.cs.core.runtime.interactor.entity.klassinstance.ArticleInstance",
  setBaseType: "com.cs.runtime.interactor.entity.KlassInstanceSet",
  collectionKlassInstanceEntityBaseType: "com.cs.runtime.interactor.entity.KlassInstanceCollection",
  assetBaseType: "com.cs.core.runtime.interactor.entity.klassinstance.AssetInstance",
  assetInstanceCollection: "com.cs.runtime.interactor.entity.AssetInstanceCollection",
  marketBaseType: "com.cs.core.runtime.interactor.entity.klassinstance.MarketInstance",
  targetInstanceCollection: "com.cs.runtime.interactor.entity.TargetInstanceCollection",

  editorialInstanceBaseType: "com.cs.runtime.interactor.entity.EditorialInstance",
  editorialCollectionInstanceBaseType: "com.cs.runtime.interactor.entity.EditorialInstanceCollection",

  attributeInstanceBaseType: "com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance",
  tagInstanceBaseType: "com.cs.core.runtime.interactor.entity.tag.TagInstance",
  roleInstanceBaseType: "com.cs.core.runtime.interactor.entity.role.RoleInstance",
  userBaseType: "com.cs.core.config.interactor.entity.user.User",

  imageAttributeInstanceBaseType: "com.cs.core.runtime.interactor.entity.propertyinstance.ImageAttributeInstance",
  collectionKlassInstanceBaseType: "com.cs.runtime.interactor.model.KlassInstanceCollectionModel",
  assetInstanceType: "com.cs.core.runtime.interactor.entity.klassinstance.AssetAttributeInstance",
  setAttributeInstanceBaseType: "com.cs.runtime.interactor.entity.SetAttributeInstance",

  articleKlassBaseType: "com.cs.core.config.interactor.entity.klass.ProjectKlass",
  setKlassBaseType: "com.cs.core.config.interactor.entity.klass.SetKlass",
  collectionKlassBaseType: "com.cs.core.config.interactor.entity.klass.CollectionKlass",
  assetKlassBaseType: "com.cs.core.config.interactor.entity.klass.Asset",
  marketKlassBaseType: "com.cs.core.config.interactor.entity.klass.Market",

  supplierBaseType: "com.cs.core.runtime.interactor.entity.supplierinstance.SupplierInstance",
  textAssetBaseType: "com.cs.core.runtime.interactor.entity.textassetinstance.TextAssetInstance",

  supplierKlassBaseType: "com.cs.core.config.interactor.entity.supplier.Supplier",
  textAssetKlassBaseType: "com.cs.core.config.interactor.entity.textasset.TextAsset",
  taskInstanceBaseType: "com.cs.core.runtime.interactor.entity.taskinstance.TaskInstance",
  taskKlassBaseType: "com.cs.core.config.interactor.entity.task.Task",

  linkedInstancesPropertyBaseType: "com.cs.core.config.interactor.model.asset.LinkedInstancesPropertyModel",
  dateInstanceBaseType: "com.cs.core.config.interactor.model.attribute.DateInstanceModel",
  OnboardingFileInstanceBaseType: "com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance",

  smartDocumentTemplateBaseType: "smartDocumentTemplate",
  smartDocumentPresetBaseType: "smartDocumentPreset",
};
