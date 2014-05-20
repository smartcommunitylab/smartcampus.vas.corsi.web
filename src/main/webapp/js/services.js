var app = angular.module('dev', [ 'ngResource', 'ngCookies', 'filters',
		'$strap.directives' ]);

app.controller('MainCtrl', function($scope, $http, $window, $location) {

	$scope.departmentList = [];
	$scope.degreesList = [];
	$scope.coursesList = [];
	$scope.department = {
		'id' : '0',
		'description' : 'Department'
	};
	$scope.degree = {
		'cdsId' : '0',
		'cdsCod' : '0',
		'descripion' : 'Degree',
		'durata' : '0',
		'aaOrd' : '0',
		'pds' : {},
		'dipartimento' : {
			'description' : 'Department',
			'id' : '0'
		},
		'id' : '0'
	};
	$scope.course = {
		'adId' : '0',
		'adCod' : '0',
		'description' : 'Course',
		'courseDescription' : 'null',
		'cds_id' : '0',
		'ordYear' : '0',
		'offYear' : '0',
		'valutazione_media' : '0',
		'rating_contenuto' : '0',
		'rating_carico_studio' : '0',
		'rating_lezioni' : '0',
		'rating_materiali' : '0',
		'rating_esame' : '0'
	};
	$scope.app;
	$scope.info = "";
	$scope.number = 0;
	$scope.average = 0;
	$scope.comments = [];

	$scope.reload = function() {
		$scope.init();
	};

	$scope.init = function() {

		$http({
			method : 'GET',
			url : 'rest/dipartimento/all',
			params : {},
			headers : {}
		}).success(function(data) {
			$scope.departmentList = data;
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};

	if ($scope.app != undefined)
		$scope.init();

	document.getElementById("developer").innerHTML = user_name;

	
	
	$scope.loadDegrees = function(dep) {

		$http({
			method : 'GET',
			url : 'rest/corsolaurea/' + dep.id,
			params : {},
			headers : {}
		}).success(function(data) {
			$scope.degreesList = data;
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};

	$scope.loadCourses = function(deg) {

		$http({
			method : 'GET',
			url : 'rest/attivitadidattica/corsolaurea/' + deg.id,
			params : {},
			headers : {}
		}).success(function(data) {
			$scope.coursesList = data;
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};

	$scope.setCurrentDep = function(dep) {
		$scope.department = dep;
		$scope.initdropdownDegree();
		$scope.initdropdownCourse();
		$scope.loadDegrees(dep);
	};

	$scope.setCurrentDeg = function(deg) {
		$scope.degree = deg;
		$scope.initdropdownCourse();
		$scope.loadCourses(deg);
	};

	$scope.setCurrentCourse = function(course) {
		$scope.course = course;
		$scope.updateCommentsCourse(course);
	};

	$scope.initdropdownDepartment = function() {
		$scope.department = {
			'id' : '0',
			'description' : 'Department'
		};
	};

	$scope.initdropdownDegree = function() {
		$scope.degree = {
			'cdsId' : '0',
			'cdsCod' : '0',
			'descripion' : 'Degree',
			'durata' : '0',
			'aaOrd' : '0',
			'pds' : {},
			'dipartimento' : {
				'description' : 'Department',
				'id' : '0'
			},
			'id' : '0'
		};
	};

	$scope.initdropdownCourse = function() {
		$scope.course = {
			'adId' : '0',
			'adCod' : '0',
			'description' : 'Course',
			'courseDescription' : 'null',
			'cds_id' : '0',
			'ordYear' : '0',
			'offYear' : '0',
			'valutazione_media' : '0',
			'rating_contenuto' : '0',
			'rating_carico_studio' : '0',
			'rating_lezioni' : '0',
			'rating_materiali' : '0',
			'rating_esame' : '0'
		};

	};
	
	
	$scope.updateCommentsCourse = function(course) {

		$http({
			method : 'GET',
			url : 'corso/' + course.adId + '/commento/all',
			params : {},
			headers : {}
		}).success(function(data) {
			$scope.comments = data;
			$scope.number = data.length;
			
			var averValue = 0;
			for (var i=0; i<data.length; i++) {
				averValue = averValue + ((data.rating_contenuto + data.rating_carico_studio + data.rating_lezioni + data.rating_materiali + data.rating_esame)/5);
			}
			
			$scope.average = averValue / data.length;
			
			// $scope.info = 'Find latest comments inserted';
			// $scope.error = '';
		}).error(function(data) {
			$scope.info = 'Error!';
			// $scope.error = "No comments found";
		});
	};
	
});



angular.module('filters', []).filter('truncate', function() {
	return function(text, length, end) {
		if (isNaN(length))
			length = 60;

		if (end === undefined)
			end = "...";

		if (text.length <= length || text.length - end.length <= length) {
			return text;
		} else {
			return String(text).substring(0, length - end.length) + end;
		}

	};
}).filter('dateformat', function() {
	return function(text, length, end) {
		return new Date(text).toLocaleString();
	};
}).filter('startFrom', function() {
	return function(input, start) {
		start = +start; // parse to int
		return input.slice(start);
	};
}).filter('nullString', function() {
	return function(input) {
		if (input == "null")
			return "";
		else
			return input;
	};
});
