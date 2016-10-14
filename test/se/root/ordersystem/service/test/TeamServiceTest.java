package se.root.ordersystem.service.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.model.Team;
import se.root.ordersystem.model.User;
import se.root.ordersystem.repository.interfaces.TeamRepository;
import se.root.ordersystem.repository.interfaces.UserRepository;
import se.root.ordersystem.service.TeamService;

@RunWith(MockitoJUnitRunner.class)

public final class TeamServiceTest {

	@Mock
	private TeamRepository teamRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private TeamService teamService;

	private static Team team1;
	private static Team team2;
	private static Team team3;

	private static List<Team> teams;

	private static User user1;
	private static User user2;
	private static User user3;
	private static List<User> users;

	@BeforeClass
	public static void setUp() {

		users = new ArrayList<>();
		teams = new ArrayList<>();

		team1 = new Team.TeamBuilder("team1").setId("1").build();
		team2 = new Team.TeamBuilder("team2").setId("2").build();
		team3 = new Team.TeamBuilder("team3").setId("3").build();

		for (int i = 1; i <= 3; i++) {
			teams.add(new Team.TeamBuilder("team " + i).setId("" + i + "").build());
		}

		user1 = new User.UserBuilder("usernr1", "test", "test").setTeamId("1").build();
		user2 = new User.UserBuilder("usernr2", "test", "test").setTeamId("1").build();
		user3 = new User.UserBuilder("usernr3", "test", "test").setTeamId("1").build();

		users.add(user1);
		users.add(user2);
	}

	@Test
	public void getUserFromTeamByTeamId() throws ServiceException, RepositoryException {

		String teamId = "1";

		when(teamRepository.getUsersFromTeam(teamId)).thenReturn(users);
		List<User> usersFromTeam = teamService.getUsersFromTeam(teamId);
		for (User u : usersFromTeam) {
			assertEquals(u.getTeamId(), teamId);
		}

		verify(teamRepository).getUsersFromTeam(teamId);
	}

	@Test
	public void createTeam() throws ServiceException, RepositoryException {

		teamService.createTeam(team1);
		verify(teamRepository).create(team1);
	}

	@Test
	public void updateTeam() throws ServiceException, RepositoryException {

		Team updateTeam = new Team.TeamBuilder("team updated").setId("2").build();

		when(teamRepository.read(team2.getId())).thenReturn(team2);
		assertEquals(updateTeam.getId(), team2.getId());

		teamService.updateTeam(updateTeam);
		verify(teamRepository).update(updateTeam);

	}

	@Test
	public void activeTeam() throws ServiceException, RepositoryException {

		String teamId = "1";
		boolean isActive = true;

		teamService.activateTeam(teamId);
		verify(teamRepository).changeStatus(isActive, teamId);
	}

	@Test
	public void inactivateTeam() throws RepositoryException, ServiceException {

		String teamId = "2";
		boolean isActive = false;

		teamService.inactivateTeam(teamId);
		verify(teamRepository).changeStatus(isActive, teamId);
	}

	@Test
	public void getAllTeams() throws RepositoryException, ServiceException {

		when(teamRepository.getAll()).thenReturn(teams);
		teamService.getAllTeams();

		verify(teamRepository).getAll();
	}

	@Test
	public void addUserToTeamSkipOneIfTeamIsFull() throws ServiceException, RepositoryException {

		String userId = "1";
		String fullTeamId = "1";

		List<User> fullTeam = new ArrayList<>();
		for (int i = 1; i <= 10; i++) {
			fullTeam.add(new User.UserBuilder("usernr" + i + "", "test", "test").setId("" + i + "")
					.setTeamId(fullTeamId).build());
		}

		when(userRepository.read(userId)).thenReturn(user1);
		when(teamRepository.getAll()).thenReturn(teams);
		when(teamRepository.getUsersFromTeam(fullTeamId)).thenReturn(fullTeam);

		long resultTeamId = teamService.addUserToTeam(userId);

		assertNotSame(resultTeamId, Long.valueOf(fullTeamId));
		verify(teamRepository).addUserToTeam(userId, String.valueOf(resultTeamId));
	}

}