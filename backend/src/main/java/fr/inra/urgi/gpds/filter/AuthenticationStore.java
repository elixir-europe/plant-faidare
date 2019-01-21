package fr.inra.urgi.gpds.filter;

import fr.inra.urgi.gpds.repository.http.UserGroupsResourceClient;

/**
 * Store user authentication (later use in {@link UserGroupsResourceClient})
 *
 * @author gcornut
 */
public final class AuthenticationStore {

	private static final ThreadLocal<String> store = new ThreadLocal<>();

	public static String get() {
		return store.get();
	}

	public static void set(String userName) {
	    store.set(userName);
	}

	public static void reset() {
	    store.set(null);
    }

}
