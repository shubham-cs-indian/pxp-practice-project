var socket;
var bufferedLogs = [];
var sWebSocketURL = '';

//Messages posted by logger will be received here
onmessage = function (event) {

  if(typeof event.data == "object" && event.data.mode == 'START_SERVER') {
    sWebSocketURL = event.data.url;
    openConnection(event.data.url);

  } else {
    //readyState:1 Connected
    if (socket && socket.readyState == 1) {
      socket.send(JSON.stringify(event.data));

    //readyState:3 Closed
    } else if(socket.readyState == 3) {
      bufferedLogs.push(event.data);
      openConnection(sWebSocketURL);

    }else {
      bufferedLogs.push(event.data);

    }
  }

};


function openConnection (url) {
  //socket = new WebSocket(url, "echo-protocol");
  socket = new WebSocket(url);

  socket.addEventListener("open", function (event) {
    while (bufferedLogs.length > 0) {
      var log = bufferedLogs.shift();
      socket.send(JSON.stringify(log));
    }
    taskCompletedSignalByWorker('WebSocket Connection Opened');
  });

  // Display messages received from the server
  socket.addEventListener("message", function (event) {
    taskCompletedSignalByWorker("Server Says: " + event.data);
  });

  // Display any errors that occur
  socket.addEventListener("error", function (event) {
    taskCompletedSignalByWorker("Error in WebSocket Connection");
  });

  socket.addEventListener("close", function (event) {
    if(event.reason && event.reason.toLowerCase().indexOf("frame size") > -1) {
      console.error("WebSocket Frame socket exceeded");
      console.error();
    }
    taskCompletedSignalByWorker("WebSocket Connection Closed");
  });
}

function taskCompletedSignalByWorker (sMessage) {
  postMessage(sMessage);
}