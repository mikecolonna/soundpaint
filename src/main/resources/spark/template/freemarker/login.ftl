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
        <li><a href="/workspace">Workspace</a></li>
        <li><a href="/projects">Projects</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li class="active"><a href="/login">Login</a></li>
        <li><a href="/register">Register</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>

<div class="error">
  ${error}
</div>

<div class="container" style="text-align: center">
  <img src="images/panda_before.jpg" style="width:30%">
  <form action="/login" method="post">
    <ul class="flex-outer" style="text-align: left">
      <li>
        <input type="email" id="email" name="email" placeholder="Email*">
        <label for="email" class="my-label">Email*</label>
        <label for="email" class="my-error">required</label>
      </li>
      <li>
        <input type="password" id="password" name="password" placeholder="Password*">
        <label for="password" class="my-label">Password*</label>
        <label for="password" class="my-error">required</label>
      </li>
      <li style="justify-content: center; margin-top: 3%">
        <input class="my-button red-button" type="submit" value="Login">
      </li>
      <li style="justify-content: center; margin-top: 3%">
        <a href="/register">Register</button>
      </li>
    </ul>
  </form>
</div>

<script type="text/javascript">
  $('.container').find('input, select, option').on('keyup blur focus click', function (e) {

  let $this = $(this)
    if (e.type === 'keyup') {
      if ($this.val() === '') {
        $(this).parent().children().eq(1).animate({ opacity: 0 });
      } else {
        $(this).parent().children().eq(1).animate({ opacity: .9 });
      }
    } else if (e.type === 'blur') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 });
      } else {
        $(this).parent().children().eq(1).animate({ opacity: .9 });
      }
    } else if (e.type === 'focus') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 });
      }
      else if($this.val() !== '') {
        $(this).parent().children().eq(1).animate({ opacity: .9 });
      }
    } else if (e.type === 'click') {
      if($this.val() === '' || $this.val() == null) {
        $(this).parent().children().eq(1).animate({ opacity: 0 });
      }
      else if($this.val() !== '') {
        $(this).parent().children().eq(1).animate({ opacity: .9 });
      }
    }
});

$('#login').click(function(e) {
  e.preventDefault();
  let email = $('#email').val();
  let password = $('#password').val();
  if(email.indexOf('@') === -1) { //look for this character
    $("#email").addClass("required-field");
    if(email === '') {
      $("#email").parent().children().eq(2).text("Email required");
    } else {
      $("#email").parent().children().eq(2).text("Please enter a proper email");
    }
    $("#email").parent().children().eq(2).animate({ opacity: 1 });
  } else {
    if($("#email").hasClass("required-field")) {
      $("#email").removeClass("required-field");
      $("#email").parent().children().eq(2).animate({ opacity: 0 });
    }
  }
  if(password.length < 6){
    $("#password").addClass("required-field");
    if(password === '') {
      $("#password").parent().children().eq(2).text("Password required");
    } else {
      $("#password").parent().children().eq(2).text("Password must be at least 6 characters long");
    }
    $("#password").parent().children().eq(2).animate({ opacity: 1 });
  } else {
    if($("#password").hasClass("required-field")) {
      $("#password").removeClass("required-field");
      $("#password").parent().children().eq(2).animate({ opacity: 0 });
    }
  }
  if(!($("#email").hasClass("required-field") && $("#password").hasClass("required-field"))) {
    $(this).unbind('submit').submit();
  }
});
</script>


</#assign>
<#include "main.ftl">
