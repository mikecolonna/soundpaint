$(document).ready(() => {
  // AUDIO
  let audio;
  let levelsData = [];
  let waveData = [];
  let levelHistory = []; //last 256 ave norm levels
  let levelBins;
  let binCount;
  let levelsCount;
  let audioContext;
  let audioBuffer;
  let buffer;
  let analyser;
  let freqByteData; // bars
  let timeByteData; // waveform
  let msecAvg;
  let timer;
  let isPlayingAudio = false;

  var beatCutOff = 0;
	var beatTime = 0;

  // VISUALS
  let camera, renderer, scene;
  let vizHolder;

  function init() {
    initAudio();
    initVisuals();
  }

  function render() {

  }

  function update() {
    requestAnimationFrame(update);

    updateAudio();
    updateVisuals();

    renderer.render(scene, camera);
  }

  function initAudio() {
    audio = document.getElementById("myAudio");

    //Get an Audio Context
    window.AudioContext = window.AudioContext || window.webkitAudioContext;
	  audioContext = new window.AudioContext();

		analyser = audioContext.createAnalyser();
		analyser.smoothingTimeConstant = 0.3; //smooths out bar chart movement over time
		analyser.fftSize = 1024;
		analyser.connect(audioContext.destination);
		binCount = analyser.frequencyBinCount; // = 512

		levelBins = Math.floor(binCount / levelsCount); //number of bins in each level

		freqByteData = new Uint8Array(binCount);
		timeByteData = new Uint8Array(binCount);

		const length = 256;
		for (let i = 0; i < length; i++) {
		    levelHistory.push(0);
		}

    // BPM timer
    // assume 120BPM
		msecsAvg = 640;
		timer = setInterval(onBeat,msecsAvg);
  }

  // ------- AUDIO -------- //

  function initSound(){
		audio = audioContext.createBufferSource();
		audio.connect(analyser);
	}

  function playAudio() {
    audio.buffer = audioBuffer;
		audio.loop = true;
		audio.play();
		isPlayingAudio = true;
  }

  function stopAudio() {
    isPlayingAudio = false;
		if (audio) {
			audio.stop(0);
			audio.disconnect();
		}
  }

  function onBeat() {
    gotBeat = true;
    //events.emit("onBeat");
  }

  function updateAudio() {
    if (!isPlayingAudio) return;

		//GET DATA
		analyser.getByteFrequencyData(freqByteData); //<-- bar chart
		analyser.getByteTimeDomainData(timeByteData); // <-- waveform

		//normalize waveform data
		for(var i = 0; i < binCount; i++) {
			waveData[i] = ((timeByteData[i] - 128) / 128) * 0.5;
		}

		//normalize levelsData from freqByteData
		for(var i = 0; i < levelsCount; i++) {
			var sum = 0;
			for(var j = 0; j < levelBins; j++) {
				sum += freqByteData[(i * levelBins) + j];
			}
			levelsData[i] = sum / levelBins / 256 * 0.5; //freqData maxs at 256

		}

		//GET AVG LEVEL
		var sum = 0;
		for(var j = 0; j < levelsCount; j++) {
			sum += levelsData[j];
		}

		volume = sum / levelsCount;

		levelHistory.push(volume);
		levelHistory.shift(1);

		//BEAT DETECTION
		if (volume  > beatCutOff && volume > BEAT_MIN){
			onBeat();
			beatCutOff = volume *1.1;
			beatTime = 0;
		}else{
			if (beatTime <= ControlsHandler.audioParams.beatHoldTime){
				beatTime ++;
			}else{
				//beatCutOff *= ControlsHandler.audioParams.beatDecayRate;
				beatCutOff = Math.max(beatCutOff,BEAT_MIN);
			}
		}


		bpmTime = (new Date().getTime() - bpmStart)/msecsAvg;
		//trace(bpmStart);
  }

  // -------- VISUALS --------- //

  function initVisuals() {
    // CAMERA
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
    camera.position.set(0, 0, 100);
    camera.lookAt(new THREE.Vector3(0, 0, 0));

    // RENDERER
    renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
    //renderer.setSize(window.innerWidth, window.innerHeight);

    // SCENE
    scene = new THREE.Scene();
    scene.fog = new THREE.Fog(0x000000, 2000, 3000);

    // INIT VISUALS
    vizHolder = new THREE.Object3D();
    scene.add(vizHolder);
    Lines.init();
  }

  function updateVisuals() {
    Lines.update();
  }

  const Lines = function() {

    // template
    let groupHolder;
    const LINE_COUNT = 4;
    let vertDistance;
    const fillFactor = 0.8;
    const planeWidth = 2000;
    const segments = 10;

    function init() {

      // initialize container
      groupHolder = new THREE.Object3D();
      vizHolder.add(groupHolder);
      groupHolder.position.z = 300;
      vertDistance = 1580 / LINE_COUNT;

      for (let i = 0; i < LINE_COUNT; i++) {
        let planeMaterial = new THREE.MeshBasicMaterial ({ color : 0xEBFF33});
        planeMaterial.color.setHSL(i / LINE_COUNT, 1.0, 0.5);

        const geometry = new THREE.PlaneGeometry(planeWidth, vertDistance,
          segments, segments);

        const mesh = new THREE.Mesh(geometry, planeMaterial);
        mesh.position.y = vertDistance * i - (vertDistance * LINE_COUNT) / 2;
        mesh.scale.y = (i + 1) / LINE_COUNT * fillFactor;
        groupHolder.add(mesh);
      }
    }

    function update() {
      //groupHolder.position.y = AudioHandler.getBPMTime() * vertDistance;

      // scale lines on levels
      for (let i = 0; i < LINE_COUNT; i++) {
        groupHolder.children[i].scale.y =
          levelsData[i] * levelsData[i] + 0.00001;
      }
    }

    function onBeat() {}

    function onBPMBeat() {}

    return {
      init : init,
      update : update
    };
  }();

  init();
  update();
  playAudio();
});
