<!DOCTYPE html>
<%@ page session="false"%>
<html lang="en" ng-app="dev">


<head lang="en">
<meta charset="utf-8">
<title>StudyMate Statistics Console</title>

<link href="css/bootstrap.min.css" rel="stylesheet"> 
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="css/prettify.css" rel="stylesheet">
<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>


<!-- required libraries -->

<!-- <script src="lib/jquery.min.js"></script> -->
<script src="lib/angular.js"></script>
<!-- <script src="lib/bootstrap.min.js"></script> -->
<script src="http://code.angularjs.org/1.1.2/angular-sanitize.js"></script>
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
<script src="lib/ng-csv.js"></script>
<script src="lib/ng-csv.min.js"></script> 



<script>
var token="<%=request.getAttribute("token")%>";
var user_name="<%=request.getAttribute("user")%>";
</script>



</head>



<body ng-controller="MainCtrl" data-ng-init="init()">
	<div class="container" style="width: 85%;">
		<div class="row" style="height: 65px">
			<h2>Usage statistics console of StudyMate</h2>
		</div>
		<div class="row">
			<!-- 	<div class="span6 "></div> -->
			<div class="span5 well">
				<div class="row">
					<div class="span1" style="text-align: center">
						<i class="fa fa-user fa-5x"></i>

					</div>
					<div class="span4">
						<p>
							User :<strong><span id="developer"></span></strong>
							<button style="margin-left: 70px;" class="btn btn-primary" ng-click="getAllComments()"> Load all comments </button>
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
					<div class="span2 well">

						<h5>General statistics about </h5>
						<ul>
						<li>{{department.description}}</li> 
						<li>{{degree.descripion}}</li> 
						<li>{{course.description}}</li>
						</ul>
							<span class="glyphicon glyphicon-align-justify"></span> Comments: <strong>{{number}}</strong>
						</p>
						<p>
							<span class="glyphicon glyphicon-star-empty"></span> Average: <strong>{{average.toFixed(2)}}</strong>
						</p>
						<br>
						<p>
						<button class="btn btn-primary" ng-csv="comments" csv-header="['Id', 'Date', 'Approved','Id student','Student name','Text','Contents rating','Study workload rating','Lessons rating','Materials rating','Exam rating']"  filename="comments.csv"><span class="glyphicon glyphicon-floppy-disk"></span> Export .csv </button>
						</p>
					</div>
					<div class="span10" title="Table options" >
						<div class="alert alert-info""> 
						
						<div class="span3">
							
							Filter contents: <input type="search" ng-model="filterContents"
								placeholder="Filter..." />

						</div>

						<br><span class="label label-primary">Ordered By: {{orderByField}}, Reverse Sort: {{reverseSort}}</span>
						</div>
						<table class="table table-bordered table-hover table-responsive">
							<thead>
								<tr>
									<th><a href="#" ng-click="orderByField='id'; reverseSort = !reverseSort">
          								Id <span ng-show="orderByField == 'id'"><span class="glyphicon glyphicon-chevron-up" ng-show="!reverseSort"></span><span class="glyphicon glyphicon-chevron-down" ng-show="reverseSort"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='data_inserimento'; reverseSort = !reverseSort">
          								Date <span ng-show="orderByField == 'data_inserimento'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='approved'; reverseSort = !reverseSort">
          								Approved <span ng-show="orderByField == 'approved'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='id_studente'; reverseSort = !reverseSort">
          								Id student <span ng-show="orderByField == 'id_studente'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='nome_studente'; reverseSort = !reverseSort">
          								Student's name <span ng-show="orderByField == 'nome_studente'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='testo'; reverseSort = !reverseSort">
          								Text <span ng-show="orderByField == 'testo'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
         							<th><a href="#" ng-click="orderByField='rating_contenuto'; reverseSort = !reverseSort">
          								Contents rating <span ng-show="orderByField == 'rating_contenuto'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
									<th><a href="#" ng-click="orderByField='rating_carico_studio'; reverseSort = !reverseSort">
          								Study workload rating <span ng-show="orderByField == 'rating_carico_studio'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
         							<th><a href="#" ng-click="orderByField='rating_lezioni'; reverseSort = !reverseSort">
          								Lessons rating <span ng-show="orderByField == 'rating_lezioni'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
         							<th><a href="#" ng-click="orderByField='rating_materiali'; reverseSort = !reverseSort">
          								Materials rating <span ng-show="orderByField == 'rating_materiali'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>
         							<th><a href="#" ng-click="orderByField='rating_esame'; reverseSort = !reverseSort">
          								Exam rating <span ng-show="orderByField == 'rating_esame'"><span ng-show="!reverseSort" class="glyphicon glyphicon-chevron-up"></span><span ng-show="reverseSort" class="glyphicon glyphicon-chevron-down"></span></span>
         								</a>						
         							</th>

								</tr>
							</thead>
							<tbody class="animate-repeat"
								ng-repeat="comment in comments | filter:filterContents | orderBy:orderByField:reverseSort">
								<tr class="{{setColorRowTable(comment)}}">
									<td>{{comment.id}}</td>
									<td>{{comment.data_inserimento | date:'MM/dd/yyyy h:mma'}}</td>
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