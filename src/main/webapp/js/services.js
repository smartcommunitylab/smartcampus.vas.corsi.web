var app = angular.module('dev', [ 'ngResource', 'ngCookies', 'filters',
		'$strap.directives' ]);

app.controller('MainCtrl',
		function($scope, $http, $window, $location) {
	
			$scope.departmentList = [];
			$scope.degreesList = [];
			$scope.coursesList = [];
			$scope.department = "Department";
			$scope.degree = "Degree";
			$scope.course = "course";
			$scope.app;
			$scope.info = "";
			$scope.comment = [];

			$scope.reload=function(){
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
			
			if($scope.app!=undefined)
			$scope.init();
			
			document.getElementById("developer").innerHTML=user_name;

		});




//$scope.onSelectDepartment=function(department){	
//		$http({
//			method : 'POST',
//			url : 'web/moderator/app/' + $scope.app.appId + '/add',
//			data:moderators,
//			headers : {
//				Authorization : 'Bearer ' + $scope.app.appToken
//			}
//		}).success(function(data) {
//			$scope.init();
//		}).error(function(data) {
//			$scope.init();
//
//		});
//	};


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
		if(input=="null")
			return "";
		else
			return input;
	};
});


