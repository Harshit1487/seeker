<!--
	This file is subject to the terms and conditions defined in
	'Cabin4j Customer Agreement.docx', which is part of this package.
-->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<body class="login-page">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-body">
                    	<spring:url value="/login" var="loginUrl" />		
                        <form role="form"  method="post" action="${loginUrl}">
                        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Name" name="name" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Email" name="email" type="text">
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Username" name="uid" type="text">
                                </div>
                                 <div class="form-group">
                                    <input class="form-control" placeholder="Phone Number" name="cellphone" type="text">
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Password" name="password" type="password" value="">
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="Confirm Password" name="confirmPassword" type="password" value="">
                                </div>
                                <button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>	
                            </fieldset>
                        </form>
                    </div>
                    <c:if test="${not empty msg}">
						<div class="alert login-${css} alert-dismissible text-center" role="alert">
							<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
							<strong>${msg} </strong>
						</div>
					</c:if>
                </div>
            </div>
        </div>
    </div>
</body>

</html>
