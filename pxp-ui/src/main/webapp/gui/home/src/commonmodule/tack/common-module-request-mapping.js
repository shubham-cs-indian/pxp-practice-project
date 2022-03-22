var RequestMapping = function () {
  this.className = "RequestMapping";

  this.GetConfigData = 'config/configdata';

  this.GetArticleWithoutPC = 'config/klasswithoutkp/<%=id%>';
  this.GetTextAssetWithoutPC = 'config/textassetwithoutkp/<%=id%>';
  this.GetSupplierWithoutPC = 'config/supplierwithoutkp/<%=id%>';
  this.GetAssetWithoutPC = 'config/assetwithoutkp/<%=id%>';
  this.GetTargetWithoutPC = 'config/targetwithoutkp/<%=id%>';
  this.GetEditorialWithoutPC = 'config/editorialwithoutkp/<%=id%>';
  this.GetArticleTaxonomy = 'config/articletaxonomy/<%=id%>';
  this.GetKlassAndAttributionTaxonomy = 'config/klassandattributiontaxonomy/<%=id%>';
  this.GetTaskWithGlobalPermission = "config/taskWithGlobalPermission/<%=id%>";
  this.GetSectionInfoForCollection = 'config/collection/getsectioninfoforcollection';
  this.GetClassPropertyCollection = 'config/klasses/getSectionInfoForKlass';
  this.GetTaxonomyPropertyCollection = 'config/taxonomy/getsectioninfofortaxonomy';
  this.GetLanguageInfo = 'config/languagesinfo';
  this.GetAssetImage = 'asset/<%=type%>/<%=id%>';
  this.getAll =  'config/users';
  this.getUsersByRole = 'config/usersbyrole/<%=id%>';
  this.GetAssetImage = 'asset/<%=type%>/<%=id%>';
  this.GetLanguageForUser = 'config/user/language';
};

export default new RequestMapping();