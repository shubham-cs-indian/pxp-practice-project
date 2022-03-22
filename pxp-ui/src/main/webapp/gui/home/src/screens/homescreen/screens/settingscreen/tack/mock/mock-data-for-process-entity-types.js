import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForEntityBaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import MockDataForEntityTypesDictionary from '../../tack/mock/mock-data-for-entity-types-dictionary';
import {getTranslations} from "../../../../../../commonmodule/store/helper/translation-manager";

export default function () {
  return [
    {
      id: MockDataForEntityTypesDictionary.ARTICLE,
      label: oTranslations().ARTICLE,
      type: MockDataForEntityBaseTypesDictionary.articleKlassBaseType
    },
    {
      id: MockDataForEntityTypesDictionary.ASSET,
      label: oTranslations().ASSET,
      type: MockDataForEntityBaseTypesDictionary.assetKlassBaseType,
      superScriptLabel: getTranslations().BETA,
    },
    {
      id: MockDataForEntityTypesDictionary.TEXT_ASSET,
      label: oTranslations().TEXT_ASSET,
      type: MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType,
      superScriptLabel: getTranslations().BETA,
    },

  ];
};