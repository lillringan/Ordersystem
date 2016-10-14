package se.root.ordersystem.model;


/**
 * The Class WorkItem-
 * a case is assigned to a User Features.
 *
 * @author Root Group
 * @version 1.0.
 */
public final class WorkItem extends Entity {

    private final String name;
    private final WorkItemStatus status;
    private final String issue_id;

    private WorkItem(String id, String name, WorkItemStatus status, String issue_id, boolean isActive) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.issue_id = issue_id;
        this.isActive = isActive;
    }

    /**
     * Work item builder- we use builder pattern to build WorkItem object whit a
     * public return from "static class WorkItemBuilder" to get a new workItem object
     *
     * @param name the name
     * @return the work item builder
     */
    public static WorkItemBuilder workItemBuilder(String name) {
        return new WorkItemBuilder(name);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result += 37 * id.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof WorkItem) {
            WorkItem otherTeam = (WorkItem) other;
            return id.equals(otherTeam.getId());
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WorkItemStatus getStatus() {
        return status;
    }

    public String getIssueId() {
        return issue_id;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * The Class WorkItemBuilder- why static?
     * reading and debugging easier and more efficient code because no
     * implicit object parameter has to be passed to the method.
     * This class return a public new user.
     */
    public static class WorkItemBuilder {

        private String id;
        private final String name;
        private WorkItemStatus status;
        private String issue_id;
        private boolean isActive;

        public WorkItemBuilder(String name) {
            this.id = "";
            this.name = name;
            this.status = WorkItemStatus.UNSTARTED;
            this.issue_id = "";
            this.isActive = true;
        }

        public WorkItemBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public WorkItemBuilder setStatus(WorkItemStatus status) {
            this.status = status;
            return this;
        }

        public WorkItemBuilder setIssue_id(String issue_id) {
            this.issue_id = issue_id;
            return this;
        }

        public WorkItemBuilder setIsActive(boolean active) {
            isActive = active;
            return this;
        }

        /**
         * Builds the WorkItem.
         *
         * @return a work item object with the values set in the builder
         */
        public WorkItem build() {
            return new WorkItem(id, name, status, issue_id, isActive);
        }
    }
}
