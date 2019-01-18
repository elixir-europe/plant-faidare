package fr.inra.urgi.gpds.filter;

import fr.inra.urgi.gpds.repository.http.UserGroupsResourceClient;

/**
 * Store user authentication (later use in {@link UserGroupsResourceClient})
 *
 * @author gcornut
 */
public final class AuthenticationStore {

	private static final ThreadLocal<User> store = new ThreadLocal<>();

	public static User get() {
		return store.get();
	}

	public static void setUser(String name, String authCode) {
        User user = new User(name, authCode);
        store.set(user);
	}

	public static void reset() {
	    store.set(null);
    }

	public static class User {
	    private final String name;
	    private final String authCode;

        private User(String name, String authCode) {
            this.name = name;
            this.authCode = authCode;
        }

        public String getName() {
            return name;
        }

        public String getAuthCode() {
            return authCode;
        }
    }
}
