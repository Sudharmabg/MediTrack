package com.airtribe.meditrack.interfaces;

/**
 * Searchable Interface
 * 
 * Demonstrates:
 * - Interface definition
 * - Contract for searchable entities
 * - Default methods (Java 8+)
 * 
 * @author Sudharma
 */
public interface Searchable {

    /**
     * Check if entity matches search criteria
     * 
     * @param keyword Search keyword
     * @return true if matches
     */
    boolean matchesSearchCriteria(String keyword);

    /**
     * Default method - case-insensitive search
     * Demonstrates default methods in interfaces (Java 8+)
     * 
     * @param keyword Search keyword
     * @return true if matches (case-insensitive)
     */
    default boolean matchesSearchCriteriaIgnoreCase(String keyword) {
        return matchesSearchCriteria(keyword != null ? keyword.toLowerCase() : null);
    }

    /**
     * Default method - check if searchable
     * 
     * @return true if entity is searchable
     */
    default boolean isSearchable() {
        return true;
    }
}
