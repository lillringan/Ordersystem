package se.root.ordersystem.repository;

import java.sql.SQLException;
import java.util.List;

import se.root.ordersystem.helpers.SQL;
import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.repository.interfaces.UserRepository;

import static se.root.ordersystem.helpers.DBInfo.url;
import static se.root.ordersystem.helpers.Mapper.USER_MAPPER;
import static se.root.ordersystem.helpers.Mapper.WORK_ITEM_MAPPER;

public final class MySQLUserRepository extends BaseCRUDRepository<User> implements UserRepository {

	public MySQLUserRepository() {
		super("user");
	}

	public long create(User user) throws RepositoryException {
		try {
			return new SQL(url).query("INSERT INTO user(username, firstname, lastname) values(?, ?, ?)")
					.parameter(user.getUsername()).parameter(user.getFirstname()).parameter(user.getLastname())
					.insert();
		} catch (SQLException e) {
			throw new RepositoryException("Could not insert user into database", e);
		}
	}

	public void update(User user) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE user SET username=? ,firstname=?,lastname=? WHERE id = ?")
					.parameter(user.getUsername()).parameter(user.getFirstname()).parameter(user.getLastname())
					.parameter(user.getId()).update();
		} catch (SQLException e) {
			throw new RepositoryException("Could not update user with id: " + user.getId(), e);
		}
	}

	public User read(String id) throws RepositoryException {
		return super.read(id, USER_MAPPER);
	}

	@Override
	public void changeStatus(boolean isActive, String id) throws RepositoryException {
		super.changeStatus(isActive, id);
	}

	@Override
	public List<User> getAll() throws RepositoryException {
		return super.getAll(USER_MAPPER);
	}

	@Override
	public List<User> getUsersBy(String username, String firstname, String lastname) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM user WHERE INSTR(username, ?) > 0 AND INSTR(firstname, ?) > 0 AND INSTR(lastname, ?) > 0")
					.parameter(username).parameter(firstname).parameter(lastname).many(USER_MAPPER);
		} catch (SQLException e) {
			throw new RepositoryException("Could not get users", e);
		}
	}

	@Override
	public void addWorkItemToUser(String userId, String workId) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE workitem SET user_id = ? WHERE id = ?").parameter(userId).parameter(workId)
					.insert();
		} catch (SQLException e) {
			throw new RepositoryException("could not add workItem: " + workId + " to user: " + userId, e);
		}
	}

	@Override
	public List<WorkItem> getAllWorkItemsByUser(String id) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM workitem WHERE user_id = ?").parameter(id).many(WORK_ITEM_MAPPER);
		} catch (SQLException e) {
			throw new RepositoryException("Could not get work item by status", e);
		}
	}
}
