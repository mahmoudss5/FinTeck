package BankSystem.demo.Config;

/**
 * Provides the current authenticated user's ID for use in services.
 * Allows mocking in unit tests without static mocking.
 */
public interface CurrentUserProvider {

    /**
     * @return the current user's ID from security context, or null if not authenticated
     */
    Long getCurrentUserId();
}
