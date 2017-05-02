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
  let cubeHolder;
  const LINE_COUNT = 30;
  const CUBE_COUNT = 6;
  let horiDistance;
  const fillFactor = 2;
  const planeWidth = 20;
  const segments = 10;
  let composer;
  //const centerAxis = new THREE.Vector3();

  function init() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
    camera.position.set(0, 0, 100);
    camera.lookAt(new THREE.Vector3(0, 0, 0));
    //camera.position.z = 2;

    renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.autoClear = false;
    document.body.appendChild(renderer.domElement);

    scene = new THREE.Scene();

    const light = new THREE.PointLight(0xffffff, 2.0, 120);
    light.position.set(0, 0, 100);
    scene.add(light);

    addLines();
    addCubes();

    // COMPOSERS
    composer = new THREE.EffectComposer(renderer);

    // PASSES
    const renderPass = new THREE.RenderPass(scene, camera);
    composer.addPass(renderPass);
    //renderPass.renderToScreen = true;

    const bloomPass = new THREE.BloomPass(1, 25, 5, 256);
    //bloomPass.renderToScreen = true;
    composer.addPass(bloomPass);

    var effectCopy = new THREE.ShaderPass(THREE.CopyShader);
    effectCopy.renderToScreen = true;
    composer.addPass(effectCopy);

  }

  function addLines() {
    lineHolder = new THREE.Object3D();
    scene.add(lineHolder);
    lineHolder.position.z = -300;
    horiDistance = canvas.width / LINE_COUNT;
    //lineHolder.rotation.z = Math.PI/4;

    let rotation = 0;
    for (let i = 0; i < LINE_COUNT; i++) {
      rotation = i * ((Math.PI) / LINE_COUNT);

      let planeMaterial = new THREE.MeshBasicMaterial ({ color : 0xEBFF33 });
      planeMaterial.color.setHSL((i / LINE_COUNT), 1.0, 0.5);
      console.log("line hue value : " + i / LINE_COUNT);

      const geometry = new THREE.PlaneGeometry(planeWidth, 2, segments, segments);

      const mesh = new THREE.Mesh(geometry, planeMaterial);
      //mesh.position.x = horiDistance * i - (horiDistance * LINE_COUNT) / 2;

      //console.log("z :" + mesh.position.z);
      mesh.rotateZ(rotation);
      //mesh.setRotationFromAxisAngle(centerAxis, rotation);
      console.log(rotation);
      mesh.scale.x = (i + 1) / LINE_COUNT * fillFactor;
      mesh.scale.y = 1000;
      lineHolder.add(mesh);
    }
  }

  function addCubes() {
    cubeHolder = new THREE.Object3D();
    scene.add(cubeHolder);

    for (let i = 0; i < CUBE_COUNT; i++) {
      var cubeGeometry = new THREE.BoxGeometry(3, 3, 3);
      var cubeMaterial = new THREE.MeshLambertMaterial({color:0xff0000, wireframe : false});
      //cubeMaterial.color.setHSL((i / CUBE_COUNT), 1.0, 0.5);
      var cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
      cube.castShadow = true;
      cube.receiveShadow = true;
      cube.name = i;
      cube.position.z = i * -20;
      cubeHolder.add(cube);
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
      lineHolder.children[i].scale.x = frequencyData[i] * frequencyData[i] * 0.00001;
    }

    for (let j = 0; j < CUBE_COUNT; j++) {
      const cube = cubeHolder.children[j];
      if (cube.position.z > 90) {
        cube.position.z = j * -20;
        cube.position.x = 0;
        cube.position.y = 0;
      } else {
        cube.position.z += 1;
        if (cube.name === 0) {
          cube.position.x += .1;
        } else if (cube.name === 1) {
          cube.position.x -= .1;
        } else if (cube.name === 2) {
          cube.position.x += .1;
          cube.position.y -= .1;
        } else if (cube.name === 3) {
          cube.position.x -= .1;
          cube.position.y += .1;
        } else if (cube.name === 4) {
          cube.position.x += .1;
          cube.position.y += .1;
        } else if (cube.name === 5) {
          cube.position.x -= .1;
          cube.position.y -= .1;
        }
      }
      cube.rotation.x += frequencyData[cube.id]/1000;
      cube.rotation.z += frequencyData[cube.id]/10000;
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

  //const clock = new THREE.Clock();
  function animate() {
    requestAnimationFrame(animate);
    render();
    //renderer.render(scene, camera);
    //renderer.clear();

    //const delta = clock.getDelta();
    composer.render();
  }

  init();
  animate();
  audio.play();
});
