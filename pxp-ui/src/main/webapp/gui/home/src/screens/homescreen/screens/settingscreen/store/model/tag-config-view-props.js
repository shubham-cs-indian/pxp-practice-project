/**
 * Created by DEV on 29-07-2015.
 */
import _aTagConfigGridViewData from './../../tack/mock/mock-data-for-tag-config-grid-view';

var TagConfigViewProps = (function () {

  let Props = function () {
    return {
      oActiveTag: {},
      bIsGridView: true,
      aTagConfigGridViewData: _aTagConfigGridViewData,
      oTagConfigGridViewPaginationData: {
        from: 0,
        pageSize: 20
      },
      aTagGridData: [],
      oReferencedTags: {},
    };
  };

  let oProperties = new Props();

  return {

    getActiveTag: function () {
      return oProperties.oActiveTag;
    },

    setActiveTag: function (_oActiveTag) {
      oProperties.oActiveTag = _oActiveTag;
    },

    getIsGridView: function () {
      return oProperties.bIsGridView;
    },

    getTagGridData: function () {
      return oProperties.aTagGridData;
    },

    setTagGridData: function (_aTagGridData) {
      oProperties.aTagGridData = _aTagGridData;
    },

    getReferencedTags: function () {
      return oProperties.oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

    reset: function () {
      oProperties = new Props();
    }

  }
})();

export default TagConfigViewProps;