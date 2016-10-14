package se.root.ordersystem.repository;

import java.sql.SQLException;
import java.util.List;

import se.root.ordersystem.helpers.SQL;
import se.root.ordersystem.model.Team;
import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.repository.interfaces.TeamRepository;

import static se.root.ordersystem.helpers.DBInfo.url;
import static se.root.ordersystem.helpers.Mapper.TEAM_MAPPER;
import static se.root.ordersystem.helpers.Mapper.USER_MAPPER;
import static se.root.ordersystem.helpers.Mapper.WORK_ITEM_MAPPER;

public final class MySQLTeamRepository extends BaseCRUDRepository<Team> implements TeamRepository {

	public MySQLTeamRepository() {
		super("team");
	}

	public long create(Team team) throws RepositoryException {
		try {
			return new SQL(url).query("INSERT INTO team(name) values(?)").parameter(team.getName()).insert();
		} catch (SQLException e) {
			throw new RepositoryException("could not create team with name: " + team.getName());
		}
	}

	public void update(Team team) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE team Set name=? WHERE id = ?").parameter(team.getName()).parameter(team.getId())
					.update();
		} catch (SQLException e) {
			throw new RepositoryException("Could not update team: " + team.getName());
		}
	}

	public Team read(String id) throws RepositoryException {
		return super.read(id, TEAM_MAPPER);
	}

	public void changeStatus(boolean isActive, String id) throws RepositoryException {
		super.changeStatus(isActive, id);
	}

	public List<Team> getAll() throws RepositoryException {
		return super.getAll(TEAM_MAPPER);
	}

	@Override
	public List<User> getUsersFromTeam(String id) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM user WHERE team_id = ?").parameter(id).many(USER_MAPPER);
		} catch (SQLException e) {
			throw new RepositoryException("Could not get users from team: " + id, e);
		}
	}

	@Override
	public void addUserToTeam(String userId, String teamId) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE user SET team_id = ? WHERE id = ?").parameter(teamId).parameter(userId).update();
		} catch (SQLException e) {
			throw new RepositoryException("Could not add user to team: " + userId, e);
		}
	}
}
