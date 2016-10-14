package se.root.ordersystem.model;

/**
 * The Class User-
 * a user of the system belonging to a team Features.
 *
 * @author Root Group
 * @version 1.0.
 */
public final class User extends Entity {

    private final String firstname;
    private final String lastname;
    private final String username;
    private final String teamId;

    private User(String id, String username, String firstname, String lastname, String teamId, boolean isActive) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.teamId = teamId;
        this.isActive = isActive;
    }

    /**
     * User builder- we use builder pattern to build user object whit a
     * public return from "static class UserBuilder" to get a new user object
     *
     * @param username  the username
     * @param firstname the firstname
     * @param lastname  the lastname
     * @return the user builder
     */
    public static UserBuilder userBuilder(String username, String firstname, String lastname) {
        return new UserBuilder(username, firstname, lastname);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * firstname.hashCode();
        result += 37 * lastname.hashCode();
        result += 37 * username.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof User) {
            User otherUser = (User) other;
            return username.equals(otherUser.username) && lastname.equals(otherUser.lastname);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getTeamId() {
        return teamId;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * The Class UserBuilder-why static?
     * reading and debugging easier and more efficient code because no
     * implicit object parameter has to be passed to the method.
     * This class return a public new user.
     */
    public static class UserBuilder {

        private String id;
        private final String firstname;
        private final String lastname;
        private final String username;
        private String teamId;

        private boolean isActive;

        /**
         * Instantiates a new user builder.
         *
         * @param username  the username
         * @param firstname the firstname
         * @param lastname  the lastname
         */
        public UserBuilder(String username, String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.username = username;
            this.teamId = "";
            this.isActive = true;
        }

        public UserBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder setTeamId(String teamId) {
            this.teamId = teamId;
            return this;
        }

        public UserBuilder setActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the user
         *
         * @return a user object with the values set in the builder
         */
        public User build() {
            return new User(id, username, firstname, lastname, teamId, isActive);
        }
    }
}
