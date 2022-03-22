let pdfReactorServerConfigProps = (function () {

  let Props = function () {
    return {
      bIsPdfReactorServerDirty: false,
      pdfReactorServerData: {}
    }
  };

  let oProperties = new Props();

  return {
    getPdfReactorServerData: function () {
      return oProperties.pdfReactorServerData;
    },

    setPdfReactorServerData: function (_pdfReactorServerData) {
      oProperties.pdfReactorServerData = _pdfReactorServerData;
    },

    reset: function () {
      oProperties = new Props();
    }
  }


})();

export default pdfReactorServerConfigProps;