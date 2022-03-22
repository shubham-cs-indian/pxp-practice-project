var UniqueIdentifierGenerator = (function () {

  return {
    generateUUID: function () {
      var iCurrentTimeStamp = new Date().getTime();

      var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var iRandom = (iCurrentTimeStamp + Math.random() * 16) % 16 | 0;

        iCurrentTimeStamp = Math.floor(iCurrentTimeStamp / 16);

        return (c == 'x' ? iRandom : (iRandom & 0x3 | 0x8)).toString(16);
      });

      return uuid;
    },
  }
})();

export default UniqueIdentifierGenerator;
