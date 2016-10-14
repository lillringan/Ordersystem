package se.root.ordersystem.repository.interfaces;

import se.root.ordersystem.model.Team;
import se.root.ordersystem.model.User;
import se.root.ordersystem.exception.RepositoryException;

import java.util.List;

/**
 * The Interface TeamRepository.
 * @author Root Group
 * @version 1.0.
 */
public interface TeamRepository extends CRUDRepository<Team>{


	long create(Team team) throws RepositoryException;

	void update(Team team) throws RepositoryException;

	/**
	 * Read.
	 *
	 * @param id the id
	 * @return the team
	 * @throws RepositoryException the repository exception
	 */
	Team read(String id) throws RepositoryException;

	/**
	 * Change status.
	 *
	 * @param isActive the is active
	 * @param id the id
	 * @throws RepositoryException the repository exception
	 */
	void changeStatus(boolean isActive, String id) throws RepositoryException;

	/**
	 * Gets the all.
	 *
	 * @return the all
	 * @throws RepositoryException the repository exception
	 */
	List<Team> getAll() throws RepositoryException;

    /**
     * Gets the users from team.
     *
     * @param id the id
     * @return the users from team
     * @throws RepositoryException the repository exception
     */
    List<User> getUsersFromTeam(String id) throws RepositoryException;

	/**
	 * Adds the user to team.
	 *
	 * @param userId the user id
	 * @param teamId the team id
	 * @throws RepositoryException the repository exception
	 */
	void addUserToTeam(String userId, String teamId) throws RepositoryException;
}
