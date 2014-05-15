<!DOCTYPE html>
<%@ page session="false"%>
<html lang="en" ng-app="dev">


<head lang="en">
<meta charset="utf-8">
<title>Moderation Console</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="css/prettify.css" rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css"
	rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css"
	rel="stylesheet">





<!-- required libraries -->

<script src="lib/jquery.min.js"></script>
<script src="lib/angular.js"></script>

<script src="lib/bootstrap.min.js"></script>
<script src="lib/angular-strap.js"></script>
<script src="js/services.js"></script>

<!-- optional libraries -->
<script src="lib/underscore-min.js"></script>
<script src="lib/moment.min.js"></script>
<script src="lib/fastclick.min.js"></script>
<script src="lib/prettify.js"></script>
<script src="lib/angular-resource.min.js"></script>
<script src="lib/angular-cookies.min.js"></script>
<script src="lib/moment.js"></script>



<script>
var token="<%=request.getAttribute("token")%>";
var appsFromDb=<%=request.getAttribute("appsFromDb")%>;
var user_name="<%=request.getAttribute("user")%>";
</script>



</head>



<body ng-controller="MainCtrl">
<!-- 	<div class="container" style="width: 65%;"> -->
		<div class="row" style="height: 220px">
			<h1> Usage statistics Console of StudyMate </h1>
		</div>
<!-- 		<div class="row">
			<div class="span6 "></div>
			<div class="span4 well">
				<div class="row">
					<div class="span1" style="text-align: center">
						<i class="fa fa-user fa-5x"></i>

					</div>
					<div class="span3">
						<p>
							Developer :<strong><span id="developer"></span></strong>
						</p>
						<p style="margin-top: 10px;">
							<button type="button" class="btn btn-success "
								ng-disabled="app==undefined" ng-click="reload();">
								<i class="fa fa-refresh"></i> Refresh
							</button>
						</p>
					</div>
				</div>
			</div>
		</div>

		<div class="row">



			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
						<p>Department</p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{selection.department}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="department in listDepartments"><a
									ng-click="onSelectDepartment(department)">{{department}}</a></li>
							</ul>
						</div>

						<p>Degree</p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{selection.degree}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="degree in listDegrees"><a
									ng-click="onSelectDegree(degree)">{{degree}}</a></li>
							</ul>
						</div>

						<p>Course</p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{selection.course}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="course in listCourses"><a
									ng-click="onSelectCourse(course)">{{course}}</a></li>
							</ul>
						</div>


						<h2>General statistics about {{selection.course}}</h2>

						<p>
							Comments number: <strong>{{generalInfo.number}}</strong>
						</p>
						<p>
							Average rate: <strong>{{generalInfo.average}}</strong>
						</p>
						<p>
					</div>
					<div class="span10">


						<div class="span3">

									Filter: <input type="search" ng-model="filterContents"
										placeholder="filter apps..." />

						</div>

						<table class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>Id</th>
									<th>Date</th>
									<th>Id student</th>
									<th>Name student</th>
									<th>Text</th>
									<th>Rating contents</th>
									<th>Rating study workload</th>
									<th>Rating lessons</th>
									<th>Rating materials</th>
									<th>Rating exam</th>

								</tr>
							</thead>
							<tbody class="animate-repeat"
								ng-repeat="comment in remoteComment | filter:filterContents">
								<tr>
									<td>{{comment.id}}</td>
									<td>{{comment.date | dateformat}}</td>
									<td>{{comment.id_studente}}</td>
									<td>{{comment.nome_studente}}</td>
									<td>{{comment.testo}}</td>
									<td>{{comment.rating_contenuto}}</td>
									<td>{{comment.rating_carico_studio}}</td>
									<td>{{comment.rating_lezioni}}</td>
									<td>{{comment.rating_materiali}}</td>
									<td>{{comment.rating_esame}}</td>
						</table>

					</div>
				</div>
			</div>



 


		</div>-->
</body>

</html>