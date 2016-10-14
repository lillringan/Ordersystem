package se.root.ordersystem.helpers;

import se.root.ordersystem.model.*;

public final class Mapper {
    public static final ResultMapper<Issue> ISSUE_MAPPER = (r -> Issue.issueBuilder(r.getString("title"))
            .setId(r.getString("id")).setDescription(r.getString("description")).setActive(r.getBoolean("is_active"))
            .build());

    public static final ResultMapper<WorkItem> WORK_ITEM_MAPPER = (r -> WorkItem.workItemBuilder(r.getString("name"))
            .setId(r.getString("id")).setStatus(WorkItemStatus.valueOf(r.getString("status"))).setIssue_id("issue_id")
            .setIsActive(r.getBoolean("is_active")).build());

    public static final ResultMapper<Team> TEAM_MAPPER = (r -> Team.teamBuilder(r.getString("name"))
            .setId(r.getString("id")).setIsActive(r.getBoolean("is_active")).build());

    public static final ResultMapper<User> USER_MAPPER = (r -> User
            .userBuilder(r.getString("username"), r.getString("firstname"), r.getString("lastname"))
            .setId(r.getString("id")).setTeamId(r.getString("team_id")).setActive(r.getBoolean("is_active"))
            .build());
}
