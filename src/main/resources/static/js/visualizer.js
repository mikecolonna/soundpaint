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
let scene;
let renderer;
let camera;
let lineHolder;
let cubeHolder;
const LINE_COUNT = 25;
const CUBE_COUNT = 6;
const fillFactor = 2;
const planeWidth = 20;
const segments = 10;

function initVisualizer() {
  ctx = new AudioContext();
  audio = document.getElementById('myAudio');
  audioSrc = ctx.createMediaElementSource(audio);
  analyser = ctx.createAnalyser();
  binCount = analyser.frequencyBinCount;
  audioSrc.connect(analyser);
  audioSrc.connect(ctx.destination);
  dataArray = new Uint8Array(binCount);
  frequencyData = new Uint8Array(binCount);
  init();
}

function startVisualizer() {
  animate();
}

function stopVisualizer() {
  cancelAnimationFrame(animationId);
}

function init() {
  camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, .1, 500);
  camera.position.set(0, 0, 100);
  camera.lookAt(new THREE.Vector3(0, 0, 0));

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
    cubeHolder.add(cube);
  }
}

function render() {
  // scale lines on levels
  for (let i = 0; i < LINE_COUNT; i++) {
    lineHolder.children[i].scale.x = frequencyData[i] * frequencyData[i] * 0.00001;

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
    cube.rotation.x += frequencyData[cube.id]/1000;
    cube.rotation.z += frequencyData[cube.id]/10000;
  }

  analyser.getByteFrequencyData(frequencyData); // amplitude in frequency domain
}

function animate() {
  animationId = requestAnimationFrame(animate);
  render();
  composer.render();
}
