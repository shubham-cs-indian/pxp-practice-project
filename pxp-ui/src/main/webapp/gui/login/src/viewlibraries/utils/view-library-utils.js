import CommonUtils from '../../commonmodule/util/common-utils';

var ViewLibraryUtils = (function () {

  return {

    isFirefox: function () {
      return CommonUtils.isFirefox();
    },
  };

})();

export default ViewLibraryUtils;
