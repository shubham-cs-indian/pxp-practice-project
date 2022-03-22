import UserModuleData from '../../../../../commonmodule/tack/mock-data-for-user-modules-dictionary';
import ClassCategoryMap from './class-category-constants-dictionary';

let oClassCategoryList = {
  [ClassCategoryMap.ARTICLE_CLASS]: {
    domainId: UserModuleData.ENTITY_ARTICLE_INSTANCE,
    moduleId: ClassCategoryMap.ARTICLE_CLASS
  },
  [ClassCategoryMap.ASSET_CLASS]: {
    domainId: UserModuleData.ENTITY_ASSET_INSTANCE,
    moduleId: ClassCategoryMap.ASSET_CLASS
  },
  [ClassCategoryMap.TARGET_CLASS]: {
    domainId: UserModuleData.ENTITY_MARKET_INSTANCE,
    moduleId: ClassCategoryMap.TARGET_CLASS
  },
  [ClassCategoryMap.TEXTASSET_CLASS]: {
    domainId: UserModuleData.ENTITY_TEXT_ASSET_INSTANCE,
    moduleId: ClassCategoryMap.TEXTASSET_CLASS
  },
  [ClassCategoryMap.SUPPLIER_CLASS]: {
    domainId: UserModuleData.ENTITY_SUPPLIER_INSTANCE,
    moduleId: ClassCategoryMap.SUPPLIER_CLASS
  },
};

export default oClassCategoryList;