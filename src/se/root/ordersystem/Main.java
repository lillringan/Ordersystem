package se.root.ordersystem;

import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.model.Issue;
import se.root.ordersystem.model.Team;
import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.repository.MySQLIssueRepository;
import se.root.ordersystem.repository.MySQLTeamRepository;
import se.root.ordersystem.repository.MySQLUserRepository;
import se.root.ordersystem.repository.MySQLWorkItemRepository;
import se.root.ordersystem.service.*;

import java.util.List;
import java.util.Scanner;

public class Main{
	public static void main(String[] args) {

		MySQLUserRepository mysqlUserRepository = new MySQLUserRepository();
		MySQLTeamRepository mysqlTeamRepository = new MySQLTeamRepository();
		MySQLWorkItemRepository mySQLWorkItemRepository = new MySQLWorkItemRepository();
		MySQLIssueRepository mySQLIssueRepository = new MySQLIssueRepository();

		UserService userService = new UserService(mysqlUserRepository, mySQLWorkItemRepository);
		TeamService teamService = new TeamService(mysqlTeamRepository, mysqlUserRepository);
		WorkItemService workItemService = new WorkItemService(mySQLWorkItemRepository);
		IssueService issueService = new IssueService(mySQLIssueRepository ,mySQLWorkItemRepository);
		
		try {
			// USER
			 User user = userService.createUser(User.userBuilder("somethkkinssgNew", "standing", "out").build());
			// System.out.println(user.getId());
            //userService.updateUser(User.userBuilder("awdvfdxbdfhgdgh", "standing", "out").setId("10").build());

//			 System.out.println(userService.getUserById("4").getFirstname());
			// System.out.println(userService.getUserByUsername("1user133378").getLastname());
/*			 for(User user : teamService.getUsersFromTeam("2")){
			 	System.out.println(user.getUsername());
			 }*/

            //List<User> users = userService.getUsersBy("", "", "");
            //users.forEach(user -> System.out.println(user.getFirstname()));
/*			 for(WorkItem w : userService.getAllWorkItemsByUser("4")){
			 	System.out.println(w.getName());
			 }*/
			//userService.inactivateUser("19");
//			userService.activateUser("19");

//			 TEAM
//  			 teamService.createTeam(Team.teamBuilder("awdawdawd").build());
			// teamService.updateTeam(Team.teamBuilder("team pro").setId("6").build());
			// teamService.inactivateTeam("6");
//			 for(Team team : teamService.getAllTeams()){
//			 	System.out.println(team.getName());
//			 }
//			 teamService.addUserToTeam("2");

//			 WORKITEM
			// workItemService.createWorkItem(WorkItem.workItemBuilder("awdd???").build());
//			 workItemService.changeWorkItemStatus("9", WorkItemStatus.DONE);
//			 workItemService.inactivateWorkItem("4");
//			 userService.addWorkItemToUser("19", "6");
//			 for(WorkItem w :
//				 workItemService.getWorkItemByStatus(WorkItemStatus.DONE)){
//				 System.out.println(w.getName());
//			 }
//			 for(WorkItem w : workItemService.getAllWorkItemsByTeam("4")){
//			 	System.out.println(w.getName());
//			 }
//			 for (WorkItem w : userService.getAllWorkItemsByUser("4")) {
//			 	System.out.println(w.getName());
//			 }
			
//			ISSUE
//			issueService.addIssueToWorkItem(Issue.issueBuilder("problem").setDescription("Vi har lite problem").build(), "9");
//		    issueService.updateIssue(Issue.issueBuilder("problem").setDescription("Vi har lite mer problem").setId("1").build());
//			for(WorkItem w : issueService.getAllWorkItemsWithIssue()){
//				System.out.println(w.getName());
//			}
		}catch(ServiceException e){
			e.printStackTrace();
		} 
			
	}
}