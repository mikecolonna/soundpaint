$(document).ready(() => {
  var ctx = new AudioContext();
  var audio = document.getElementById('myAudio');
  var audioSrc = ctx.createMediaElementSource(audio);
  var analyser = ctx.createAnalyser();
  //analyser.getByteTimeDomainData(dataArray);
  const binCount = analyser.frequencyBinCount;

  audioSrc.connect(analyser);
  audioSrc.connect(ctx.destination);
  var dataArray = new Uint8Array(binCount);
  var frequencyData = new Uint8Array(binCount);
  var scene, renderer, camera;
  var fov, zoom, inc;

  let lineHolder;
  const LINE_COUNT = 24;
  let horiDistance;
  const fillFactor = 0.8;
  const planeWidth = 20;
  const segments = 10;
  const RECT_SIDE = 50;
  const RECT_DEPTH = 1000;
  const LINES_PER_SIDE = LINE_COUNT / 4;
  const LINE_WIDTH = RECT_SIDE / LINES_PER_SIDE; //??
  //const centerAxis = new THREE.Vector3();

  function init() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
    camera.position.set(0, 0, 100);
    camera.lookAt(new THREE.Vector3(0, 0, 0));
    //camera.position.z = 2;

    renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
    renderer.setSize(window.innerWidth, window.innerHeight);
    document.body.appendChild(renderer.domElement);

    scene = new THREE.Scene();

    /*const light = new THREE.PointLight(0xffffff, 1.0, 100);
    light.position(0, 0, 0);
    scene.add(light);*/

    addLines();

    fov = camera.fov;
    zoom = 1.0;
    inc = -0.001;
  }

  function addLines() {
    lineHolder = new THREE.Object3D();
    scene.add(lineHolder);
    lineHolder.position.z = -300;
    horiDistance = canvas.width / LINE_COUNT;
    //lineHolder.rotation.z = Math.PI/4;

    let rotation = 0;
    for (let i = 0; i < LINE_COUNT; i++) {
      rotation = i * ((2 * Math.PI) / LINE_COUNT);

      let planeMaterial = new THREE.MeshBasicMaterial ({ color : 0xEBFF33});
      planeMaterial.color.setHSL((i / LINE_COUNT), 1.0, 0.5);
      console.log("line hue value : " + i / LINE_COUNT);

      const geometry = new THREE.PlaneGeometry(planeWidth, 2, segments, segments);
      //onst geometry = new THREE.Geometry

      const mesh = new THREE.Mesh(geometry, planeMaterial);
      mesh.position.x = horiDistance * i - (horiDistance * LINE_COUNT) / 2;

      //console.log("z :" + mesh.position.z);
      //mesh.rotateZ(Math.PI/4);
      //mesh.setRotationFromAxisAngle(centerAxis, rotation);
      console.log(rotation);
      mesh.scale.x = (i + 1) / LINE_COUNT * fillFactor;
      mesh.scale.y = 1000;
      lineHolder.add(mesh);
    }
  }

  function render() {
    /*scene.traverse(function (e) {
      if (e instanceof THREE.Mesh) {
        //e.rotation.x += frequencyData[e.id]/5000;
        e.rotation.y += frequencyData[e.id]/1000;
        //e.rotation.z += frequencyData[e.id]/10000;
        var color = new THREE.Color(1, 0, 0);
        var r = 0;
        var b = 0;
        var g = frequencyData[e.id]/255;

        //e.material.color.setRGB(r, b, g);
      }
    });*/

    // scale lines on levels
    for (let i = 0; i < LINE_COUNT; i++) {
      lineHolder.children[i].scale.x = frequencyData[i] * 0.01;
    }

    /*camera.fov = fov * zoom;
    camera.updateProjectionMatrix();
    zoom += inc;
    if ( zoom <= 0.1*(frequencyData[20]/100) || zoom >= 1*(frequencyData[20]/100) ){
      inc = -inc;
    }*/

    //analyser.getByteTimeDomainData(dataArray);  // amplitude in time domain
    analyser.getByteFrequencyData(frequencyData); // amplitude in frequency domain
  }

  function animate() {
    requestAnimationFrame(animate);
    render();

    renderer.render(scene, camera);
  }

  init();
  animate();
  audio.play();
});
