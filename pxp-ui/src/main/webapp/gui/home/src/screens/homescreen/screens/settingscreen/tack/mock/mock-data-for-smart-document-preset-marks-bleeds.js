import {getTranslations as oTranslations} from '../../../../../../commonmodule/store/helper/translation-manager';


const pdfMarksBleedsData = function () {
  return {
    aPDFMarksBleeds: [
      {
        id: "bleed",
        label: oTranslations().ADD_BLEED_BOX
      },
      {
        id: "print",
        label: oTranslations().ADD_BLEED_BOX_PRINT_MARKS
      }
    ]
  }
}

export default pdfMarksBleedsData;