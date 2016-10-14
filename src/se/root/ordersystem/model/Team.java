package se.root.ordersystem.model;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class Team-
 * a grouping of User Features, max 10 users in one team.
 *
 * @author Root Group
 * @version 1.0.
 */
public final class Team extends Entity {

    private final String name;
    private final List<User> users;

    private Team(String id, String name, List<User> users, boolean isActive) {
        this.name = name;
        this.users = users;
        this.id = id;
        this.isActive = isActive;
    }

    /**
     * Team builder- we use builder pattern to build team object whit a
     * public return from "static class TeamBuilder" to get a new Team object
     *
     * @param name the name
     * @return the team builder
     */
    public static TeamBuilder teamBuilder(String name) {
        return new TeamBuilder(name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * id.hashCode();
        result += 37 * name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Team) {
            Team otherTeam = (Team) other;
            return id.equals(otherTeam.getId()) && name.equals(otherTeam.getName());
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public Team isActive(boolean active) {
        this.isActive = active;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * The Class TeamBuilder- why static?
     * reading and debugging easier and more efficient code because no
     * implicit object parameter has to be passed to the method.
     * This class return a public new team.
     */
    public static class TeamBuilder {

        private String id;
        private final String name;
        private List<User> users;
        private boolean isActive;

        /**
         * Instantiates a new team builder.
         *
         * @param name the name of the team
         */
        public TeamBuilder(String name) {
            this.id = "";
            this.users = new ArrayList<>();
            this.isActive = true;
            this.name = name;
        }

        public TeamBuilder setUsers(List<User> users) {
            this.users = users;
            return this;
        }

        public TeamBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public TeamBuilder setIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        /**
         * Builds the team
         *
         * @return a team object with the values set in the builder
         */
        public Team build() {
            return new Team(id, name, users, isActive);
        }

    }
}
