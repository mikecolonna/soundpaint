const AudioHandler = function() {
  // source audio file
  let audio;

  function init() {
    audio = document.body.createElement("AUDIO");
  }

  function playAudio() {
    audio.play();
  }

  function stopAudio() {
    audio.stop();
  }

  function onAudioDrop(evt) {
    const audio = evt.dataTransfer.files;

    initAudioSourceFromFile(audio[0]);

    const fd = new FormData();
    fd.append('data', audio[0]);

    // callback -- after audio has been processed
    function sendAudioToViz(data) {
      // array of arrays...one array per sec?
      const frequencyData = JSON.parse(data);
      VizHandler.setData(frequencyData);
    }

    $.ajax({
      type : "POST",
      url : "/audio",
      data : fd,
      success : sendAudioToViz,
      processData : false,    // so fd is not converted to String
      contentType : false
    });
  }

  function initAudioSourceFromFile(file) {
    const fd = new FormData();
    fd.append('data', file);

    // we have to make a POST request
    // to get the URL of the audio file
    function setSourceURL(data) {
      const url = JSON.parse(data);
      audio.src = url;
    }

    $.ajax({
      type : "POST",
      url : "/url",
      data : fd,
      success : setSourceURL,
      processData : false,
      contentType : false
    });
  }
};
