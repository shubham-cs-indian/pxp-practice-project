import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionViewForCollectionEdit } from './common-config-section-replica-for-collection-edit';
import ViewUtils from './utils/view-utils';

const oEvents = {
};

const oPropTypes = {
  collectionData: ReactPropTypes.object,
  buttonClickHandler: ReactPropTypes.func
};

// @CS.SafeComponent
class CollectionEditView extends React.Component {
  constructor(props) {
    super(props);
  }

  getEditCollectionView = () => {
    let _this = this;
    let _props = _this.props;
    let oCollectionData = _props.collectionData;
    let oActiveCollection = oCollectionData.activeCollection;
    if(oCollectionData.isEditCollectionScreen) {
      let oModel = {
        label: oActiveCollection.label,
      };
      if (oActiveCollection.createdBy === ViewUtils.getCurrentUser().id) {
        oModel.isPublic = {isSelected: !oActiveCollection.isPublic, context: "collection"}
      }
      var oBodyStyle = {
        padding: '0 10px 20px 10px'
      };

      let aButtonData = [];
      if (oActiveCollection && oActiveCollection.isDirty) {
        aButtonData = [
          {
            id: "cancel",
            label: getTranslation().CANCEL,
            isDisabled: false,
            isFlat: true,
          },
          {
            id: "save",
            label: getTranslation().SAVE,
            isDisabled: false,
            isFlat: false,
          }
        ];
      } else {
        aButtonData = [
          {
            id: "ok",
            label: getTranslation().OK,
            isDisabled: false,
            isFlat: false,
          }
        ];
      }

      let oCreateDialogLayoutData = [
        {
          id: "11",
          label: getTranslation().BASIC_INFORMATION,
          elements: [
            {
              id: "1",
              label: getTranslation().NAME,
              key: "label",
              type: "singleText",
              width: 50
            },
            {
              id: "2",
              label: "Private",
              key: "isPublic",
              type: "yesNo",
              width: 50
            }
          ]
        }
      ];

      /**No need to show isPublic For children collections**/
      if(oActiveCollection.type === "staticCollection" && oActiveCollection.parentId != -1) {
         CS.remove(oCreateDialogLayoutData[0].elements, {id: "2"});
      }
      let fButtonHandler = this.props.buttonClickHandler;

      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().EDIT}
                                bodyStyle={oBodyStyle}
                                bodyClassName=""
                                contentClassName="createContextModalDialog"
                                buttonData={aButtonData}
                                onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                                buttonClickHandler={fButtonHandler}>
        <CommonConfigSectionViewForCollectionEdit context={"collection"} sectionLayout={oCreateDialogLayoutData} data={oModel}/>
      </CustomDialogView>);

    }
    else {
      return null;
    }
  };



  render () {
    return this.getEditCollectionView();
  }
}

CollectionEditView.propTypes = oPropTypes;

export const view = CollectionEditView;
export const events = oEvents;
