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
	<ul id="filters">
		<li class="filter_pair">
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
  <button class="my-button" id="new_filter">Add Filter Pair</button>
  <button class="my-button red-button" id="render">Render</button>
</div>
<script src="js/three.js"></script>
<script src="js/test.js"></script>

<audio id="myAudio" src="01 Ultralight Beam.mp3"></audio>
<canvas id="canvas">
</canvas>
<div id="frame">
  <video id="preview" autoplay>
      <source src="movie.mp4" type="video/mp4">
  </video>
</div>

<script type="text/javascript">
  function resize_canvas() {
    canvas = document.getElementById("canvas");
    canvas.width = "87%";
    canvas.height = "80%";
  };
  $(document).ready(function() {
    let max_fields = 5; //maximum input boxes allowed
    let wrapper = $("#filters"); //Fields wrapper
    let x = 1;
    console.log(x);
    $("#new_filter").on("click", function(e) {
      e.preventDefault();
      if(x < max_fields) { //max input box allowed
        x++; //text box increment
        $(wrapper).append('<li class="filter_pair"><select><option value="volvo">Volvo</option><option value="saab">Saab</option><option value="opel">Opel</option><option value="audi">Audi</option></select><select><option value="volvo">Volvo</option><option value="saab">Saab</option><option value="opel">Opel</option><option value="audi">Audi</option></select><a href="#" class="remove">remove</a></li>'); //add new filter space
        $(".remove").on("click", function(e) { //user click on remove text
          e.preventDefault();
          $(this).parent('li').remove();
          x--;
        })
      } else {
        alert("Maximum 5 filters");
      }
    });
  });
</script>
</#assign>
<#include "main.ftl">
