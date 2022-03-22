import {getTranslations as oTranslations} from '../../../../../../commonmodule/store/helper/translation-manager';


const pdfColorSpaceData = function () {
  return {
    aPDFColorSpace: [
      {
        id: "rgb",
        label: oTranslations().RGB
      },
      {
        id: "cmyk",
        label: oTranslations().CMYK
      }
    ]
  }
}

export default pdfColorSpaceData;