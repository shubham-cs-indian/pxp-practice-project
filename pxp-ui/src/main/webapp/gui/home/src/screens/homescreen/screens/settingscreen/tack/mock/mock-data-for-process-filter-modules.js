import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';

export default function () {
  return [
    {
      id: "pimmodule",
      label: oTranslations().PRODUCTS
    },
    {
      id: "mammodule",
      label: oTranslations().MAM
    }
  ];
};