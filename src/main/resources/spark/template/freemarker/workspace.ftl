<#assign content>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="/">
        <img src="images/panda_before.jpg" style="height: 27px">
      </a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="/">Home</a></li>
        <li class="active"><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="/login">Login</a></li>
        <li><a href="/register">Register</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="error">
  ${error}
</div>

<h1><span>WorkSpace</span></h1>

<div id="work" onresize="resize_canvas()">
  <form action="/sendRender" method="post">
  	<ul id="filters">
  		<li>
  			<select>
          <option value="volvo">Volvo</option>
          <option value="saab">Saab</option>
          <option value="opel">Opel</option>
          <option value="audi">Audi</option>
        </select>
  			<select>
          <option value="volvo">Volvo</option>
          <option value="saab">Saab</option>
          <option value="opel">Opel</option>
          <option value="audi">Audi</option>
        </select>
      </li>
  	</ul>
    <button class="my-button" id="new-filter">Add Filter Pair</button>
    <input class="my-button red-button" id="render" type="submit" value="Render">
  </form>

</div>
<script src="js/three.js"></script>
<script src="js/test.js"></script>

<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
<canvas id="canvas">
</canvas>
<div>
  <video id="preview" autoplay>
      <source src="movie.mp4" type="video/mp4">
  </video>

<script type="text/javascript">
  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = 87%;
    canvas.height = 80%;
  };
  $("#new-filter").click(function(e) {
    console.log("ok");
    e.preventDefault();
    console.log("YAY");
  })
</script>
</#assign>
<#include "main.ftl">
