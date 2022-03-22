import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import RequestMapping from '../../libraries/requestmappingparser/request-mapping-parser';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import { view as MSSView } from '../multiselectview/multi-select-search-view';
import MockDataForKlassTypes from '../../commonmodule/tack/mock-data-for-class-types';
import { ClassRequestMapping as oClassRequestMapping } from '../../screens/homescreen/screens/settingscreen/tack/setting-screen-request-mapping';

const oPropTypes = {
  context: ReactPropTypes.string,
  cannotRemove: ReactPropTypes.bool,

  //type list props
  isTypeListDisabled: ReactPropTypes.bool,
  isTypeListMultiSelect: ReactPropTypes.bool,
  isTypeListHidden: ReactPropTypes.bool,
  selectedType: ReactPropTypes.string,
  shouldUseGivenType: ReactPropTypes.bool,
  onTypeListChanged: ReactPropTypes.func,
  shouldShowTaxonomyType: ReactPropTypes.bool,
  excludedTypes: ReactPropTypes.array,

  //entity list props
  isKlassListDisabled: ReactPropTypes.bool,
  isKlassListMultiSelect: ReactPropTypes.bool,
  referencedKlasses: ReactPropTypes.object,
  selectedKlasses: ReactPropTypes.array,
  excludedKlasses: ReactPropTypes.array,
  onKlassListChanged: ReactPropTypes.func,
  isKlassListNatureType: ReactPropTypes.bool,
  isKlassListAbstract: ReactPropTypes.bool,
  requestResponseInfo: ReactPropTypes.object,
  updateEntityForcefully:ReactPropTypes.bool,
  isVariantAllowed: ReactPropTypes.bool,
};
/**
 * @class KlassSelectorView - use to display class selctor view in the Application.
 * @memberOf Views
 * @property {string} [context] -  context name.
 * @property {bool} [cannotRemove] - boolean for cannotRemove or not.
 * @property {bool} [isTypeListDisabled] -  boolean for isTypeListDisabled or not.
 * @property {bool} [isTypeListMultiSelect] -  boolean for isTypeListMultiSelect or not.
 * @property {bool} [isTypeListHidden] -  boolean for isTypeListHidden or not.
 * @property {string} [selectedType] -  selected type class in string.
 * @property {bool} [shouldUseGivenType] -  boolean for shouldUseGivenType or not.
 * @property {func} [onTypeListChanged] -  function which is used onTypeListChanged event.
 * @property {bool} [shouldShowTaxonomyType] -  boolean for shouldShowTaxonomyType or not.
 * @property {array} [excludedTypes] -  array of excludedTypes.
 * @property {bool} [isKlassListDisabled] -  boolean for isKlassListDisabled or not.
 * @property {bool} [isKlassListMultiSelect] -  boolean for isKlassListMultiSelect or not.
 * @property {object} [referencedKlasses] - n object of referencedKlasses.
 * @property {array} [selectedKlasses] - n array of selectedKlasses.
 * @property {array} [excludedKlasses] - n array of excludedKlasses.
 * @property {func} [onKlassListChanged] -  function which used on onKlassListChanged event.
 * @property {bool} [isKlassListNatureType] -  boolean for isKlassListNatureType or not.
 * @property {bool} [isKlassListAbstract] -  boolean for isKlassListAbstract or not.
 * @property {object} [requestResponseInfo] -  object of requestResponseInfo.
 * @property {bool} [updateEntityForcefully] -  boolean for updateEntityForcefully or not.
 * @property {bool} [isVariantAllowed] -  boolean for isVariantAllowed or not.
 */

// @CS.SafeComponent
class KlassSelectorView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      selectedType: KlassSelectorView.getRootKlassIdFromBaseType(props),
      context: this.props.context,
      isKlassSelected: false
    }
    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if ((oNextProps.context !== oState.context || !CS.isEmpty(oNextProps.selectedKlasses) || oNextProps.updateEntityForcefully) && !oState.isKlassSelected) {
      return {
        selectedType: KlassSelectorView.getRootKlassIdFromBaseType(oNextProps),
        context: oNextProps.context
      };
    } else {
      return {
        isKlassSelected: false
      };
    }
  }

  /*componentWillReceiveProps = (oNextProps) => {
    if (oNextProps.context != this.props.context || !CS.isEmpty(oNextProps.selectedKlasses) || oNextProps.updateEntityForcefully) {
      this.setState({
        selectedType: this.getRootKlassIdFromBaseType(oNextProps)
      });
    }
  }*/

  static getRootKlassIdFromBaseType = (oProps) => {
    /*let oProps = oNextProps;
    if (CS.isEmpty(oProps)) {
      oProps = this.props;
    }*/


    let sRootKlassId = ViewUtils.getRootKlassIdFromBaseType(oProps.selectedKlasses, oProps.referencedKlasses);

    //sRootKlassId is empty in case of taxonomies and if nothing is selected
    //shouldShowTaxonomyType is true if taxonomies are to be displayed
    //selected klasses data would not be empty if taxonomies are selected
    if (CS.isEmpty(sRootKlassId) && oProps.shouldShowTaxonomyType && !CS.isEmpty(oProps.selectedKlasses)) {
      sRootKlassId = "taxonomy";
    }

    return sRootKlassId;
  }

  handleKlassTypeChanged = (aSelectedItems) => {
    this.setState({
      selectedType: aSelectedItems[0],
      isKlassSelected: true
    });

    if (CS.isFunction(this.props.onTypeListChanged)) {
      this.props.onTypeListChanged(aSelectedItems)
    }
  };

  handleKlassListChanged = (aSelectedItems, oReferencedData) => {
    if (CS.isFunction(this.props.onKlassListChanged)) {
      this.props.onKlassListChanged(aSelectedItems, oReferencedData);
    }
  };

  getKlassTypeData = () => {
    let aClassData = new MockDataForKlassTypes();

    let aExcludedTypes = this.props.excludedTypes;
    if (!CS.isEmpty(aExcludedTypes)) {
      aClassData = CS.filter(aClassData, function (oKlassType) {
        return !CS.includes(aExcludedTypes, oKlassType.id);
      });
    }

    if (this.props.shouldShowTaxonomyType) {
      if(!CS.find(aClassData, {id: "taxonomy"})){
        aClassData.push({
          id: "taxonomy",
          label: getTranslation().TAXONOMY,
          value: "taxonomy"
        });
      }
    } else if (!this.props.shouldShowTaxonomyType) {
      CS.remove(aClassData, {id: "taxonomy"})
    }

    return CS.sortBy(aClassData, 'label');
  };

  getKlassTypeView = () => {
    let oProps = this.props;

    if (oProps.isTypeListHidden) {
      return null;
    }

    let sSelectedType = oProps.shouldUseGivenType ? oProps.selectedType : this.state.selectedType;

    return (
        <div className="klassSelectorMSSContainer">
          <MSSView
              disabled={oProps.isTypeListDisabled}
              items={this.getKlassTypeData()}
              selectedItems={[sSelectedType]}
              isMultiSelect={oProps.isTypeListMultiSelect}
              onApply={this.handleKlassTypeChanged}
              cannotRemove={oProps.cannotRemove}
              bShowIcon={oProps.bShowIcon}
          />
        </div>
    )
  };

  getKlassListView = () => {
    let oProps = this.props;

    let sSelectedType = oProps.shouldUseGivenType ? oProps.selectedType : this.state.selectedType;
    if (CS.isEmpty(sSelectedType)) {
      return null;
    }

    let aMockDataForKlassTypes = this.getKlassTypeData();
    let oClassData = CS.find(aMockDataForKlassTypes, {id: sSelectedType});
    let aTypes = [];
    let bShouldGetTaxonomies = oProps.shouldShowTaxonomyType && (oClassData.id === "taxonomy");

    if (!bShouldGetTaxonomies) {
      aTypes.push(oClassData.value);
    }

    let oRequestResponseInfo = !CS.isEmpty(oProps.requestResponseInfo) ? oProps.requestResponseInfo : {
      requestType: "customType",
      responsePath: ["success", "list"],
      requestURL: RequestMapping.getRequestUrl(oClassRequestMapping.GetKlassesListByBaseType),
      customRequestModel: {
        types: aTypes,
        isNature: oProps.isKlassListNatureType,
        isAbstract: oProps.isKlassListAbstract,
        isVariantAllowed :  oProps.isVariantAllowed,
        shouldGetAttributionTaxonomies: bShouldGetTaxonomies,
      }
    };

    return (
        <div className="klassSelectorMSSContainer">
          <LazyMSSView
              isMultiSelect={oProps.isKlassListMultiSelect}
              disabled={oProps.isKlassListDisabled}
              selectedItems={oProps.selectedKlasses}
              excludedItems={oProps.excludedKlasses}
              context={sSelectedType}
              referencedData={oProps.referencedKlasses}
              requestResponseInfo={oRequestResponseInfo}
              onApply={this.handleKlassListChanged}
              cannotRemove={oProps.cannotRemove}
              ref={this.klassSelectorContainer}
              bShowIcon={oProps.bShowIcon}
          />
        </div>
    )
  };

  render () {

    return (<div className="klassSelectorContainer" ref ={this.setRef.bind(this, "klassSelectorContainer")}>
      <div className="klassSelectorBoxContainer">
        {this.getKlassTypeView()}
      </div>
      <div className="klassSelectorBoxContainer klassSelectorKlassList">
        {this.getKlassListView()}
      </div>
    </div>);
  }

}

KlassSelectorView.propTypes = oPropTypes;

export const view = KlassSelectorView;
