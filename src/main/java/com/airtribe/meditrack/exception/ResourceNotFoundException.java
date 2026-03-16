package com.airtribe.meditrack.exception;

/**
 * Exception thrown when a resource is not found
 * 
 * @author Sudharma
 */
public class ResourceNotFoundException extends MediTrackException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with ID: %d", resourceName, id),
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s not found with identifier: %s", resourceName, identifier),
                "RESOURCE_NOT_FOUND");
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
