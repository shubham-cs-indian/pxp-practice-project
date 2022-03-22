import CS from '../../libraries/cs';
import React, {Component} from "react";

import PropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import ContentComparisonSelectLanguage from "./content-comparision-select-language-view";
import StepperView from "../stepperview/stepper-view";
import {getTranslations} from '../../commonmodule/store/helper/translation-manager.js';
import {view as ChipView} from "../../viewlibraries/chipsView/chips-view";
import {view as ContentComparisonMatchAndMergeView} from './content-comparison-mnm-view';
import { view as GoldenRecordMnmMatchPropertiesView } from './golden-record-mnm-match-properties-view';
import GRConstants from '../../screens/homescreen/screens/contentscreen/tack/golden-record-view-constants';
import {view as ContextMenuViewNew} from './../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import {view as ImageFitToContainerView} from './../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
const getLabelOrCode = CS.getLabelOrCode;


const oEvents = {
  CONTENT_COMPARISON_MNM_LANGUAGE_CHANGED: "content_comparison_mnm_language_changed",
};

class ContentComparisonView extends Component {

  constructor (props) {
    super(props);
    this.state = {
      classes: {
        stepperRoot: "contentComparisonStepperRoot",
        labelContainer: "contentComparisonLabelContainer"
      }
    };
  }

  handleMNMLanguageForComparisonChanged = (oLanguageData) => {
    EventBus.dispatch(oEvents.CONTENT_COMPARISON_MNM_LANGUAGE_CHANGED, oLanguageData.id);
  }

  getGoldenRecordMatchPropertiesView () {
    let oActiveBucketData = this.props.activeBucketData;
    let oConfigDetails = this.props.configDetails;
    if (!CS.isEmpty(oActiveBucketData)) {
      return (
          <div className="goldenRecordMatchPropertyContainer">
            <div className="propertiesHeader">{"Matched Properties"}</div>
            <GoldenRecordMnmMatchPropertiesView
                aAttributes={oActiveBucketData.attributes}
                oReferencedAttributes={oConfigDetails.referencedAttributes}
                aTags={oActiveBucketData.tags}
                oReferencedTags={oConfigDetails.referencedTags}
                oMultiTaxonomyData={oActiveBucketData.multiTaxonomyData}
                aKlassesData={oActiveBucketData.klassesData}
                aUserList={this.props.userList}/>
          </div>
      )
    }
    return null;
  };

  getSelectedLanguageChipView () {
    let {contentComparisonSkeleton: {selectedContentIds: aSelectedContentIds}, contentComparisonSelectedLanguageData: aContentComparisonSelectedLanguageData} = this.props;
    let oView = null;
    if(!!aSelectedContentIds.length) {
      oView = (<div className="selectLanguageSummary">
        <div className={"selectedLanguagesHeader"}>{getTranslations().SELECTED_LANGUAGES}</div>
        <ChipView items={aContentComparisonSelectedLanguageData}/>
      </div>);
    }
    return oView;
  }

  getSelectedPropertiesAndLanguagesView () {
    return (
        <div className={"goldenRecordPropertiesAndLanguageWrapper"}>
          {this.getGoldenRecordMatchPropertiesView()}
          {this.getSelectedLanguageChipView()}
        </div>
    )
  };

  getLanguageSelectionGridView () {
    let {contentComparisonSkeleton: oContentComparisonSkeleton, contentComparisonSelectionGridData: aContentComparisonSelectionGridData} = this.props;
    let oMatchPropertiesView = this.getGoldenRecordMatchPropertiesView();
    let oSelectedLanguageView = this.getSelectedLanguageChipView();

    return <ContentComparisonSelectLanguage
        contentComparisonSelectionGridData={aContentComparisonSelectionGridData}
        contentComparisonSkeleton={oContentComparisonSkeleton}
        selectedLanguageView={oSelectedLanguageView}
        matchPropertiesView={oMatchPropertiesView}
    />
  }

  getMatchAndMergeLanguageView () {
    let oContentComparisonMatchAndMergeData = this.props.contentComparisonMatchAndMergeData;
    let oLanguageData = oContentComparisonMatchAndMergeData.languageData;

    if (CS.isEmpty(oLanguageData)) {
      return;
    }
    let oSelectedItem = oLanguageData.selectedItem;
    let sSelectedItemURL = oLanguageData.selectedItemURL;
    let oLanguageSelectionData = oLanguageData.rowData;
    oLanguageSelectionData.onClickHandler = this.handleMNMLanguageForComparisonChanged;

    return (
        <div className="languageViewWrapper">
          <div className="languageHeader">{oLanguageData.tableHeaderLabel}</div>
          <div className="languageForComparisonContainer">
            <ContextMenuViewNew {...oLanguageSelectionData}>
            <div className={"languageSelectorContainer"}>
              <div className="languageSelectorIcon">
                <ImageFitToContainerView imageSrc={sSelectedItemURL}/>
              </div>
              <div className="languageSelectorLabel">{getLabelOrCode(oSelectedItem)}</div>
              <div className="buttonIcon"/>
            </div>
          </ContextMenuViewNew>
          </div>
        </div>
    )
  }

  getMatchAndMergeView () {
    let {
      contentComparisonActivePropertyDetailedData: oContentComparisonMatchAndMergeActivePropertyDetailedData,
      contentComparisonMatchAndMergeData: oContentComparisonMatchAndMergeData
    } = this.props;

    let oLanguageData = oContentComparisonMatchAndMergeData.languageData;
    let oContainerStyle = {
      height: "calc(100% - 160px)"
    }
    if(CS.isEmpty(oLanguageData)) {
      oContainerStyle.height = "calc(100% - 74px)"
    }


    return (
        <div className="contentComparisonMNMContainerWrapper">
          {this.getSelectedPropertiesAndLanguagesView()}
          {this.getMatchAndMergeLanguageView()}
          <ContentComparisonMatchAndMergeView
              context={GRConstants.GOLDEN_RECORD_COMPARISON}
              contentComparisonMatchAndMergeData={oContentComparisonMatchAndMergeData}
              activePropertyDetailedData={oContentComparisonMatchAndMergeActivePropertyDetailedData}
              containerStyle={oContainerStyle}
          />
        </div>
    );
  };

  getStepsView () {
    let iStepperViewActiveStep = this.props.stepperViewActiveStep;
    switch (iStepperViewActiveStep) {
      case 0:
        return this.getLanguageSelectionGridView();

      case 1:
      case 2:
        return this.getMatchAndMergeView();

      default:
        return this.getLanguageSelectionGridView();
    }
  }

  getViewToRender () {
    let {stepperViewActiveStep: iStepperViewActiveStep, stepperViewSteps: aStepperViewSteps, contentComparisonSkeleton: {selectedContentIds: aSelectedContentIds},
      activeGoldenRecordId: sActiveGoldenRecordId, isFullScreenMode: bIsFullScreenMode} = this.props;
    let sFinishLabel = '';
    let sFinishButtonId = '';

    if (CS.isEmpty(sActiveGoldenRecordId)) {
      sFinishLabel = getTranslations().CREATE;
      sFinishButtonId = GRConstants.CREATE;
    } else {
      sFinishLabel = getTranslations().SAVE;
      sFinishButtonId = GRConstants.SAVE;
    }

    return (
        <div className="contentComparisonStepperViewContainer">
          <StepperView activeStep={iStepperViewActiveStep || 0}
                       view={this.getStepsView()}
                       steps={aStepperViewSteps || []}
                       finishLabel={sFinishLabel}
                       finishButtonId={sFinishButtonId}
                       context={"contentComparison"}
                       classes={this.state.classes}
                       disabledNext={!aSelectedContentIds.length}
                       orientation={"horizontal"}/>
        </div>
    );
  }

  render () {
    return (
        <div className="contentComparisonViewDialogViewContainer">
          {this.getViewToRender()}
        </div>
    );
  }
}

ContentComparisonView.propTypes = {
  contentComparisonSkeleton: PropTypes.object,
  contentComparisonSelectionGridData: PropTypes.array,
  contentComparisonIds: PropTypes.object,
  stepperViewActiveStep: PropTypes.number,
  stepperViewSteps: PropTypes.array,
  contentComparisonSelectedLanguageData: PropTypes.array,
  contentComparisonActivePropertyDetailedData: PropTypes.object,
  contentComparisonMatchAndMergePropertiesDetailedData: PropTypes.object,
  contentComparisonMatchAndMergeData: PropTypes.object,
  activeBucketData: PropTypes.object,
  configDetails: PropTypes.object,
  userList: PropTypes.array,
  headerLabel: PropTypes.string,
  activeGoldenRecordId: PropTypes.string,
  isFullScreenMode: PropTypes.bool
};

export default ContentComparisonView;
export var events = oEvents;
