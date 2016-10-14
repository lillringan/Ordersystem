package se.root.ordersystem.service.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.repository.interfaces.UserRepository;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;
import se.root.ordersystem.service.UserService;

@RunWith(MockitoJUnitRunner.class)

public final class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private WorkItemRepository workItemRepository;

	@InjectMocks
	private UserService userService;

	private static User user1;
	private static User user2;
	private static List<User> users;
	private static WorkItem workItem;
	private static List<WorkItem> workItems;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUp() {

		user1 = new User.UserBuilder("usernr1234", "test", "testsson").setId("1").build();
		user2 = new User.UserBuilder("usernr2", "test", "testsson").setId("2").setActive(false).build();

		users = new ArrayList<>();
		users.add(user1);
		users.add(user2);

		workItems = new ArrayList<>();
		workItem = new WorkItem.WorkItemBuilder("problem?").setId("1").build();
		workItems.add(workItem);
	}

	@Test
	public void createUser() throws RepositoryException, ServiceException {

		userService.createUser(user1);
		verify(userRepository).create(user1);
	}

	@Test
	public void ShouldThrowExceptionWhenUsernameIsTooShort() throws ServiceException {
		thrown.expect(ServiceException.class);
		thrown.expectMessage("Username too short, 10 characters required");
		userService.createUser(new User.UserBuilder("123", "Test", "Test").build());
	}

	@Test
	public void getUserById() throws ServiceException, RepositoryException {
		String userId = "2";
		String username = "usernr2";
		String firstname = "test";
		String lastname = "test";

		when(userRepository.read(userId)).thenReturn(user2);
		User user = userService.getUserById(userId);
		assertEquals(username, user.getUsername());
		verify(userRepository).read(userId);
	}

	@Test
	public void updateUser() throws ServiceException, RepositoryException {

		User updatedUser = new User.UserBuilder("updatedUser", "test", "testsson").setId("1").build();

		when(userRepository.read(updatedUser.getId())).thenReturn(user1);
		assertEquals(updatedUser.getId(), user1.getId());

		userService.updateUser(updatedUser);
		verify(userRepository).update(updatedUser);

	}

	@Test
	public void activateUserById() throws ServiceException, RepositoryException {

		String userId = "1";
		boolean isActive = true;

		userService.activateUser(userId);
		verify(userRepository).changeStatus(isActive, userId);
	}

	@Test
	public void inactivateUserById() throws ServiceException, RepositoryException {

		String userId = "1";
		boolean isActive = false;

		userService.inactivateUser(userId);
		verify(userRepository).changeStatus(isActive, userId);
	}

	@Test
	public void getAllUsers() throws ServiceException, RepositoryException {

		when(userRepository.getAll()).thenReturn(users);
		userService.getAll();
		verify(userRepository).getAll();
	}

	@Test
	public void getUsersByUsername() throws RepositoryException, ServiceException {

		User DBUser1 = new User.UserBuilder("username", "first", "last").setId("2").build();
		User DBUser2 = new User.UserBuilder("username", "first", "last").setId("3").build();

		List<User> DBusers = new ArrayList<>();
		DBusers.add(DBUser1);
		DBusers.add(DBUser2);

		when(userRepository.getUsersBy(DBUser1.getUsername(), null, null)).thenReturn(DBusers);
		userService.getUsersBy(DBUser1.getUsername(), null, null);

		verify(userRepository).getUsersBy(DBUser1.getUsername(), null, null);
	}

	@Test
	public void getUsersByFirstname() throws RepositoryException, ServiceException {

		User DBUser1 = new User.UserBuilder("username", "first", "last").setId("2").build();
		User DBUser2 = new User.UserBuilder("username", "first", "last").setId("3").build();

		List<User> DBusers = new ArrayList<>();
		DBusers.add(DBUser1);
		DBusers.add(DBUser2);

		when(userRepository.getUsersBy(null, DBUser1.getFirstname(), null)).thenReturn(DBusers);
		userService.getUsersBy(null, DBUser1.getFirstname(), null);

		verify(userRepository).getUsersBy(null, DBUser1.getFirstname(), null);
	}

	@Test
	public void getUsersByLastname() throws RepositoryException, ServiceException {

		User DBUser1 = new User.UserBuilder("username", "first", "last").setId("2").build();
		User DBUser2 = new User.UserBuilder("username", "first", "last").setId("3").build();

		List<User> DBusers = new ArrayList<>();
		DBusers.add(DBUser1);
		DBusers.add(DBUser2);

		when(userRepository.getUsersBy(null, null, DBUser1.getLastname())).thenReturn(DBusers);
		userService.getUsersBy(null, null, DBUser1.getLastname());

		verify(userRepository).getUsersBy(null, null, DBUser1.getLastname());
	}

	@Test
	public void addWorkItemToUser() throws ServiceException, RepositoryException {

		String userId = "1";
		String workId = "1";

		when(userRepository.read(userId)).thenReturn(user1);
		when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(workItems);

		userService.addWorkItemToUser(userId, workId);
		verify(userRepository).addWorkItemToUser(userId, workId);
	}

	@Test
	public void shouldThrowIfUserIsNotActive() throws RepositoryException, ServiceException {

		thrown.expect(ServiceException.class);
		thrown.expectMessage("Could not add work item to user since it's inactive");

		String userId = "2";
		String workId = "1";

		when(userRepository.read(userId)).thenReturn(user2);
		when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(workItems);

		userService.addWorkItemToUser(userId, workId);
	}

	@Test
	public void shouldThrowIfUserHasFiveWorkItems() throws ServiceException, RepositoryException {

		thrown.expect(ServiceException.class);
		thrown.expectMessage("Could not add work item to user since it already has 5 work items");

		String userId = "2";
		String workId = "1";

		List<WorkItem> userWorkItems = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			userWorkItems.add(workItem);
		}
		when(userRepository.read(userId)).thenReturn(user1);
		when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(userWorkItems);

		userService.addWorkItemToUser(userId, workId);

	}

	@Test
	public void getAllWorkItemsByUser() throws RepositoryException, ServiceException {

		String userId = "1";

		when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(workItems);
		userService.getAllWorkItemsByUser(userId);
		verify(userRepository).getAllWorkItemsByUser(userId);

	}

}
