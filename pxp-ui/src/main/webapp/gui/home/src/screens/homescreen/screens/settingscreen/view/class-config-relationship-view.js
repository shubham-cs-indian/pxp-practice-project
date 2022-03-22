import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as KlassSelectorView } from '../../../../../viewlibraries/klassselectorview/klass-selector-view';
import oSectionLayout from '../tack/class-config-layout-data';
import RelationshipTypeDictionary from '../../../../../commonmodule/tack/relationship-type-dictionary';
import MockDataForEntityBaseTypesDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ViewUtils from './utils/view-utils';
import SettingUtils from "../store/helper/setting-utils";

const oEvents = {
  CLASS_RELATIONSHIP_MODIFIED: "class_relationship_modified"
};

const oPropTypes = {
  relationshipLayoutModel: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
  activeKlass: ReactPropTypes.object,
  isEntityDirty: ReactPropTypes.bool
};

// @CS.SafeComponent
class ClassConfigRelationshipView extends React.Component {
  static propTypes = oPropTypes;

  handleClassRelationshipTypeDropDownValueChanged = (sRelationshipId, sKey, oReferencedKlasses) => {
    EventBus.dispatch(oEvents.CLASS_RELATIONSHIP_MODIFIED, sRelationshipId, sKey, "", oReferencedKlasses);
  };

  handleClassRelationshipDropDownValueChanged = (sRelationshipId, sKey, aItems, oReferencedKlasses) => {
    let sKlassId = !CS.isEmpty(aItems) ? aItems[0] : "";
    EventBus.dispatch(oEvents.CLASS_RELATIONSHIP_MODIFIED, sRelationshipId, sKey, sKlassId, oReferencedKlasses);
  };

  getNecessaryInformation = () => {
    let oActiveKlass = this.props.activeKlass;
    let oModel = this.props.relationshipLayoutModel;
    let oRelationship = CS.find(oActiveKlass.relationships, {id: oModel.id});

    let sSelectedClassType = null;
    let bTypeListHidden = false;
    let bShouldUseGivenType = false;
    let aExcludedKlasses = [];
    let bIsClassListDisabled = false;
    let oRequestResponseInfo = {};
    let bIsKlassListNatureType = undefined;
    let bIsKlassListAbstract = undefined;
    let bIsVariantAllowed  = undefined;

    let sRelType = oRelationship.relationshipType;
    if (ViewUtils.isVariantRelationship(oRelationship.relationshipType)) {
      /**Info: Show ALL TYPES of classes for linked variant*/
      /**And Exclude self */
      aExcludedKlasses.push(oActiveKlass.id);
      sSelectedClassType = ViewUtils.getKlassSelectorViewEntityTypeByClassType(oActiveKlass.type);
      bTypeListHidden = true;
      bShouldUseGivenType = true;
    }
    else if (
        ViewUtils.isFixedBundleRelationship(sRelType) ||
        ViewUtils.isSetOfProductsRelationship(sRelType)
    ) {
      bTypeListHidden = true;
      /**Info: Show ALL TYPES of classes for linked variant*/
    }

    return {
      selectedType: sSelectedClassType,
      isTypeListHidden: bTypeListHidden,
      shouldUseGivenType: bShouldUseGivenType,
      isClassListDisabled: bIsClassListDisabled,
      excludedKlasses: aExcludedKlasses,
      requestResponseInfo: oRequestResponseInfo,
      isKlassListNatureType: bIsKlassListNatureType,
      isKlassListAbstract: bIsKlassListAbstract,
      isVariantAllowed: bIsVariantAllowed,
    }
  };

  getSectionData = () => {
    let oActiveKlass = this.props.activeKlass;
    let oModel = this.props.relationshipLayoutModel;
    let oRelationship = CS.find(oActiveKlass.relationships, {id: oModel.id});
    let oReferencedData = this.props.referencedData;
    if(oActiveKlass.type === MockDataForEntityBaseTypesDictionary.articleKlassBaseType || oActiveKlass.type === MockDataForEntityBaseTypesDictionary.assetKlassBaseType ){
      let bIsAfterSaveEnabled = oRelationship.enableAfterSave || false;
      let sSplitter = SettingUtils.getSplitter();
      oModel.enableAfterSave = {
        context: "class_relationship" + sSplitter + oRelationship.id,
        isSelected: bIsAfterSaveEnabled
      };
    }

    let oNecessaryInformation = this.getNecessaryInformation();
    let sSide2KlassId = oRelationship.side2.klassId;
    let oReferencedKlasses = oReferencedData.referencedKlasses;
    if(sSide2KlassId === oActiveKlass.id && CS.isEmpty(oReferencedKlasses[oActiveKlass.id])){
      oReferencedKlasses[oActiveKlass.id] = oActiveKlass;
    }

    let aSelectedKlasses = !CS.isEmpty(sSide2KlassId) ? [sSide2KlassId] : [];
    oModel.side2klass = (
        <KlassSelectorView
          context={oActiveKlass.id}
          cannotRemove={true}
          referencedKlasses={oReferencedData.referencedKlasses}
          selectedKlasses={aSelectedKlasses}
          excludedKlasses={oNecessaryInformation.excludedKlasses}
          isKlassListNatureType={oNecessaryInformation.isKlassListNatureType}
          selectedType={oNecessaryInformation.selectedType}
          shouldUseGivenType={oNecessaryInformation.shouldUseGivenType}
          isTypeListHidden={oNecessaryInformation.isTypeListHidden}
          isKlassListDisabled={oNecessaryInformation.isClassListDisabled}
          requestResponseInfo={oNecessaryInformation.requestResponseInfo}
          onTypeListChanged={this.handleClassRelationshipTypeDropDownValueChanged.bind(this, oRelationship.id, "side2klass", oReferencedData)}
          onKlassListChanged={this.handleClassRelationshipDropDownValueChanged.bind(this, oRelationship.id, "side2klass")}
          updateEntityForcefully={!this.props.isEntityDirty}
          isKlassListAbstract={oNecessaryInformation.isKlassListAbstract}
          isVariantAllowed={oNecessaryInformation.isVariantAllowed}
      />
    );

    if(oModel.hasOwnProperty("taxonomyInheritanceSetting")) {
      oModel.taxonomyInheritanceSetting.onApply = this.handleClassRelationshipDropDownValueChanged.bind(this, oRelationship.id, "taxonomyInheritanceSetting");
    }
    return oModel;
  };

  getRelationshipTitle = (sRelationshipType="") => {
    switch (sRelationshipType) {
      case RelationshipTypeDictionary.PRODUCT_VARIANT:
        return getTranslation().PRODUCT_VARIANT_RELATIONSHIP;

      case RelationshipTypeDictionary.PROMOTIONAL_VERSION:
        return getTranslation().PROMOTIONAL_VERSION_RELATIONSHIP;

      default:
        return "";
    }
  };

  getRelationshipSectionData = (sRelationshipType = "") => {
    let SectionLayout = new oSectionLayout();
    switch (sRelationshipType) {
      case RelationshipTypeDictionary.PRODUCT_VARIANT:
      case RelationshipTypeDictionary.PROMOTIONAL_VERSION:
        return CS.cloneDeep(SectionLayout[sRelationshipType]);
      default :
        return CS.cloneDeep(SectionLayout.classRelationship);
    }
  };

  render() {
    var sSplitter = ViewUtils.getSplitter();
    var oModel = this.props.relationshipLayoutModel;
    var sRelationshipType = oModel.relationshipType;
    var aSectionLayout = this.getRelationshipSectionData(sRelationshipType);
    var sHeaderTitle = this.getRelationshipTitle(sRelationshipType);
    var sRelationshipTitle = "classRelationshipHeader";
    if (CS.isEmpty(sHeaderTitle)) {
      sRelationshipTitle += " hideTitle";
    }

    var sContext = "class_relationship" + sSplitter + oModel.id;
    var aDisabledFields = ["code"];
    return (
        <div>
          <div className="classRelationshipContainer">
            <div className={sRelationshipTitle}>
              <div className="classSectionHeaderTitle">{sHeaderTitle}</div>
            </div>
            <CommonConfigSectionView context={sContext}
                                     sectionLayout={aSectionLayout}
                                     disabledFields={aDisabledFields}
                                     data={this.getSectionData()}/>
          </div>
        </div>
    );
  }
}

export const view = ClassConfigRelationshipView;
export const events = oEvents;
