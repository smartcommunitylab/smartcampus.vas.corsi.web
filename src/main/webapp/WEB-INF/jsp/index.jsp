<!DOCTYPE html>
<%@ page session="false"%>
<html lang="en" ng-app="dev">


<head lang="en">
<meta charset="utf-8">
<title>StudyMate Statistics Console</title>

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
var user_name="<%=request.getAttribute("user")%>";
</script>



</head>



<body ng-controller="MainCtrl" data-ng-init="init()">
	<div class="container" style="width: 80%;">
		<div class="row" style="height: 65px">
			<h2>Usage statistics Console of StudyMate</h2>
		</div>
		<div class="row">
			<!-- 	<div class="span6 "></div> -->
			<div class="span5 well">
				<div class="row">
					<div class="span1" style="text-align: center">
						<i class="fa fa-user fa-5x"></i>

					</div>
					<div class="span3">
						<p>
							User :<strong><span id="developer"></span></strong>
						</p>
						<p style="margin-top: 10px;">
							<button type="button" class="btn btn-success"
								ng-click="reload();">
								<i class="fa fa-refresh"></i> Refresh
							</button>
						</p>
						<p style="margin-top: 25px;">Filter contents by:</p>
						<p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{department.description}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="dep in departmentList"><a ng-click="setCurrentDep(dep)">{{dep.description}}</a></li>
							</ul>
						</div>
						</p>
						<p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{degree.descripion}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="deg in degreesList"><a ng-click="setCurrentDeg(deg)">{{deg.descripion}}</a></li>
							</ul>
						</div>
						</p>
						<p>
						<div class="btn-group">
							<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
								{{course.description}} <span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<li ng-repeat="course in coursesList">
									<a ng-click="setCurrentCourse(course)">{{course.description}}</a>
								</li>
							</ul>
						</div>
						</p>
					</div>
				</div>
			</div>
		</div>

		<div class="row">

			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">


						<!-- 						<p>Degree</p>
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
						</div> -->


						<h5>General statistics about </h5>
						<ul>
						<li>{{department.description}}</li> 
						<li>{{degree.descripion}}</li> 
						<li>{{course.description}}</li>
						</ul>
						<p>
							Comments number: <strong>{{number}}</strong>
						</p>
						<p>
							Average rate: <strong>{{average}}</strong>
						</p>
					</div>
					<div class="span10" >


						<div class="span3">

							Filter contents: <input type="search" ng-model="filterContents"
								placeholder="Filter..." />

						</div>

						<table class="table table-striped table-bordered table-responsive">
							<thead>
								<tr>
									<th>Id</th>
									<th>Date</th>
									<th>Approved</th>
									<th>Id student</th>
									<th>Name student</th>
									<th>Text</th>
									<th>Contents rating</th>
									<th>Study workload rating</th>
									<th>Lessons rating</th>
									<th>Materials rating</th>
									<th>Exam rating</th>

								</tr>
							</thead>
							<tbody class="animate-repeat"
								ng-repeat="comment in comments | filter:filterContents">
								<tr class="{{setColorRowTable(comment)}}">
									<td>{{comment.id}}</td>
									<td>{{comment.date | date:'yyyy-MM-dd HH:mm:ss'}}</td>
									<td>{{comment.approved}}</td>
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



		</div>


	</div>
</body>

</html>