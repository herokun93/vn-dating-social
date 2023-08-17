package vn.dating.app.gateway.controllers;



class EmployeeNotFoundException extends RuntimeException {

    EmployeeNotFoundException() {
        super("Could not find employee ");
    }
}
