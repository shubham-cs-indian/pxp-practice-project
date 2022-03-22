import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewLibraryUtils from './../utils/view-library-utils';

const oEvents = {

};

const oPropTypes = {
  attributeInstance: ReactPropTypes.object.isRequired,
  masterAttribute: ReactPropTypes.object.isRequired,
  userList: ReactPropTypes.array
};
/**
 * @class AttributeReadOnlyView - Use in thumbnail goldenrecord bucket view for attriubutes read only views.
 * @memberOf Views
 * @property {object} attributeInstance - Pass data of attribute like attributeId, code, context, jobId, tags, value
 * etc.
 * @property {object} masterAttribute - Pass data of attributes like data, id, icon, code, attributeTags, label,
 * type etc.
 * @property {array} [userList] - Pass Array of user of attributes and in that info of user like birthdate, id,
 * label, gender, icon, firstName, email, contact etc.
 */

// @CS.SafeComponent
class AttributeReadOnlyView extends React.Component {

  static propTypes = oPropTypes;

  constructor(props) {
    super(props);

    this.state = {
    };
  }

  render () {

    let oAttribute = this.props.attributeInstance;
    let oMasterAttribute = this.props.masterAttribute;
    let aUserList = this.props.userList;
    let oAttributeValue = null;

    if (!CS.isEmpty(oAttribute.value)) {
      if (ViewLibraryUtils.isAttributeTypeUser(oMasterAttribute.type)) {
        let oUser = CS.find(aUserList, {id: oAttribute.value});
        oAttributeValue = !CS.isEmpty(oUser) ? (oUser.lastName + " " + oUser.firstName) : "";
      } else if (ViewLibraryUtils.isAttributeTypeHtml(oMasterAttribute.type)) {
        var oDangerousHtmlObj = {__html: oAttribute.valueAsHtml};
        oAttributeValue = (
            <div className="disconnectedHtml" dangerouslySetInnerHTML={oDangerousHtmlObj}></div>
        );
      } else {
        oAttributeValue = ViewLibraryUtils.getLabelByAttributeType(oMasterAttribute.type, oAttribute.value, oMasterAttribute.defaultUnit,
            oMasterAttribute.precision, oMasterAttribute.hideSeparator);
      }
    }

    return (
        <div className="attributeReadOnlyValue">{oAttributeValue}</div>
    );

  };
}

export const view = AttributeReadOnlyView;
export const events = oEvents;
