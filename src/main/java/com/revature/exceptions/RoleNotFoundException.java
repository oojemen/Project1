package com.revature.exceptions;


//package com.revature.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("Could not find user role");
    }
}