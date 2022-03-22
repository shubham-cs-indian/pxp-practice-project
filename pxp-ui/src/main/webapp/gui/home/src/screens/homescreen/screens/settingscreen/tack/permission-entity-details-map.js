import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const permissionEntityDetailsMap = function () {
  return {
    "classes":{id: "classes", label: oTranslations().CLASSES},
    "articles": {id: 'articles', label: oTranslations().ARTICLES_LABEL},
    "assets": {id: 'assets', label: oTranslations().ASSETS},
    "markets": {id: 'markets', label: oTranslations().MARKET},
    "suppliers": {id: 'suppliers', label: oTranslations().SUPPLIER},
    /*"targets": {id: 'targets', label: oTranslations().MARKET},*/
    "textAssets": {id: 'textAssets', label: oTranslations().TEXT_ASSET},
    "taxonomies": {id: 'taxonomies', label: oTranslations().TAXONOMIES},
    "contexts": {id: 'contexts', label: oTranslations().CONTEXTS},
    "tasks": {id: 'tasks', label: oTranslations().TASKS}
  };
};

export default permissionEntityDetailsMap;