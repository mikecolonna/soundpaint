// AUDIO HANDLING
let ctx;
let audio;
let audioSrc;
let analyser;
let binCount;
let dataArray;
let frequencyData;

// VISUALS
let animationId;
let animationData;
let scene;
let renderer;
let camera;
let light;
let lineHolder;
let cubeHolder;
let currFrame;
let totalFrames = 1;
let all_objects = [];
const LINE_COUNT = 25;
const CUBE_COUNT = 6;
const fillFactor = 2;
const planeWidth = 20;
const segments = 10;

function initVisualizerAudio() {
  ctx = new AudioContext();
  audio = document.getElementById('myAudio');
  audioSrc = ctx.createMediaElementSource(audio);
  analyser = ctx.createAnalyser();
  binCount = analyser.frequencyBinCount;
  audioSrc.connect(analyser);
  audioSrc.connect(ctx.destination);
}

function setAnimationData(animdata) {

  animationData = animdata;
  totalFrames = Object.keys(animationData).length;
}

function startVisualizer() {
  animate();
}

function stopVisualizer() {
  cancelAnimationFrame(animationId);
}

function destroyScene() {
  // for (let i = 0; i < LINE_COUNT; i++) {
  //   lineHolder.remove(children[i]);
  //   lineHolder.children[i].mesh.dispose();
  //   lineHolder.children[i].geometry.dispose();
  //   lineHolder.children[i].material.dispose();
  // }
  //
  // for (let j = 0; j < CUBE_COUNT; j++) {
  //   cubeHolder.remove(children[j]);
  //   cubeHolder.children[j].mesh.dispose();
  //   cubeHolder.children[j].geometry.dispose();
  //   cubeHolder.children[j].material.dispose();
  // }
    all_objects.forEach(function (obj) {
        scene.remove(obj);
    });
  scene.remove(lineHolder);
  scene.remove(cubeHolder);


}

function initVisualizer() {
  dataArray = new Uint8Array(binCount);
  frequencyData = new Uint8Array(binCount);
  currFrame = 0;

  camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
  camera.position.set(0, 0, 100);
  camera.lookAt(new THREE.Vector3(0, 0, 0));

  renderer = new THREE.WebGLRenderer( { canvas : document.getElementById('canvas') } );
  renderer.setSize(window.innerWidth, window.innerHeight);
  renderer.autoClear = false;
  document.body.appendChild(renderer.domElement);

  scene = new THREE.Scene();


  light = new THREE.PointLight(0xffffff, 2.0, 120);
  light.position.set(0, 0, 100);
  scene.add(light);
  all_objects.push(light);

  addLines();
  addCubes();

  // COMPOSER
  composer = new THREE.EffectComposer(renderer);

  // PASSES
  const renderPass = new THREE.RenderPass(scene, camera);
  composer.addPass(renderPass);

  const bloomPass = new THREE.BloomPass(1, 25, 5, 256);
  composer.addPass(bloomPass);

  const effectCopy = new THREE.ShaderPass(THREE.CopyShader);
  effectCopy.renderToScreen = true;
  composer.addPass(effectCopy);
}

function addLines() {
  lineHolder = new THREE.Object3D();
  scene.add(lineHolder);
  lineHolder.position.z = -300;
  horiDistance = canvas.width / LINE_COUNT;

  let rotation = 0;
  for (let i = 0; i < LINE_COUNT; i++) {
    rotation = i * ((Math.PI) / LINE_COUNT);

    let planeMaterial = new THREE.MeshBasicMaterial ({ color : 0xEBFF33 });
    planeMaterial.color.setHSL((i / LINE_COUNT), 1.0, 0.5);
    const geometry = new THREE.PlaneGeometry(planeWidth, 2, segments, segments);
    const mesh = new THREE.Mesh(geometry, planeMaterial);

    mesh.rotateZ(rotation);
    mesh.scale.x = (i + 1) / LINE_COUNT * fillFactor;
    mesh.scale.y = 1000;
    all_objects.push(mesh);
    lineHolder.add(mesh);
  }
}

function addCubes() {
  cubeHolder = new THREE.Object3D();
  scene.add(cubeHolder);

  for (let i = 0; i < CUBE_COUNT; i++) {
    const cubeGeometry = new THREE.BoxGeometry(3, 3, 3);
    const cubeMaterial = new THREE.MeshLambertMaterial({color:0xff0000, wireframe : false});
    const cube = new THREE.Mesh(cubeGeometry, cubeMaterial);
    cube.castShadow = true;
    cube.receiveShadow = true;
    cube.name = i;
    cube.position.z = i * -20;
    all_objects.push(cube);
    cubeHolder.add(cube);
  }
}

function render() {
  // scale lines on levels
    if (currFrame < totalFrames) {

  for (let i = 0; i < LINE_COUNT; i++) {
    if ($("#pulse").is(':checked')) {
        lineHolder.children[i].scale.x = frequencyData[i] * frequencyData[i] * 0.00001;
    } else if ($("#strobe").is(':checked')) {
        let currData = animationData[currFrame.toString()];
        lineHolder.children[i].scale.x = currData[i.toString()] * currData[i.toString()] * 0.0001;
    }

    if ($("#setRgb").is(':checked')) {
      const r = $("#red").val();
      const g = $("#green").val();
      const b = $("#blue").val();
      lineHolder.children[i].material.color.setRGB(r, g, b);
    } else if ($("#setRainbow").is(':checked')) {
      lineHolder.children[i].material.color.setHSL((i / LINE_COUNT), 1.0, 0.5);
    }
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

    if ($("#pulse").is(':checked')) {
        cube.rotation.x += frequencyData[cube.id]/1000;
        cube.rotation.z += frequencyData[cube.id.toString()]/10000;
    } else if ($("#strobe").is(':checked')) {
        cube.rotation.x += currData[cube.id.toString()]/1000;
        cube.rotation.z += currData[cube.id.toString()]/10000;
    }
  }

  analyser.getByteFrequencyData(frequencyData); // amplitude in frequency domain
    //console.log(frequencyData);
    } else {
      console.log(totalFrames);
    }
}

function resetSoundCounter() {
  currFrame = 0;
}

function animate() {
  if($("#strobe").is(':checked')) {
    currFrame++;
  }
  animationId = requestAnimationFrame(animate);
  render();
  composer.render();
}
