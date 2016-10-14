package se.root.ordersystem.model;

/**
 * The Class Issue
 * Issue is a remark that can be given a work item when it is not accepted and the Work item is in status DONE
 *
 * @author Root Group
 * @version 1.0.
 */
public final class Issue extends Entity {

    private final String title;
    private final String description;

    private Issue(String id, String title, String description, boolean isActive) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
    }

    /**
     * Issue builder-we use builder pattern to build issue object whit a
     * public return from "static class IssueBuilder" to get a new Issue object
     *
     * @param title the title
     * @return the issue builder
     */
    public static IssueBuilder issueBuilder(String title) {
        return new IssueBuilder(title);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * id.hashCode();
        result += 37 * title.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Issue) {
            Issue otherIssue = (Issue) other;
            return id.equals(otherIssue.id) && title.equals(otherIssue.title);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    /**
     * The Class IssueBuilder- why static?
     * reading and debugging easier and more efficient code because no
     * implicit object parameter has to be passed to the method.
     * This class return a public new issue.
     */
    public static class IssueBuilder {

        private String id;
        private final String title;
        private String description;
        private boolean isActive;

        /**
         * Instantiates a new issue builder.
         *
         * @param title The title of the issue
         */
        public IssueBuilder(String title) {
            this.id = "";
            this.title = title;
            this.description = "";
            this.isActive = true;
        }

        public IssueBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public IssueBuilder setActive(boolean active) {
            isActive = active;
            return this;
        }

        public IssueBuilder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Builds the issue.
         *
         * @return an issue object with the values set in the builder
         */
        public Issue build() {
            return new Issue(id, title, description, isActive);
        }
    }
}
