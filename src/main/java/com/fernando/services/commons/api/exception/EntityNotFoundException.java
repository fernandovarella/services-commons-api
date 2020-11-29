package com.fernando.services.commons.api.exception;

public class EntityNotFoundException  extends RuntimeException {
    
    private static final long serialVersionUID = -7672142589479553699L;

    public EntityNotFoundException() {
        super("Entity not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
