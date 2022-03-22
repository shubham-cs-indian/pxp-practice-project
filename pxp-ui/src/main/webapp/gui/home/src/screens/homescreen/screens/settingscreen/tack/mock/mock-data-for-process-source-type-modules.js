import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';

export default function () {
  return [
    {
      id: "product",
      label: oTranslations().PRODUCT
    },
    {
      id: "asset",
      label: oTranslations().ASSET
    }
  ];
};