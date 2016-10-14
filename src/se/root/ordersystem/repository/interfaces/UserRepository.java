package se.root.ordersystem.repository.interfaces;

import java.util.List;

import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.exception.RepositoryException;


/**
 * The Interface UserRepository.
 * @author Root Group
 * @version 1.0.
 */
public interface UserRepository extends CRUDRepository<User> {

	long create(User user) throws RepositoryException;

	void update(User user) throws RepositoryException;
	
	/**
	 * Read.
	 *
	 * @param id the id
	 * @return the user
	 * @throws RepositoryException the repository exception
	 */
	User read(String id) throws RepositoryException;
	
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
	List<User> getAll() throws RepositoryException;

	List<User> getUsersBy(String username, String lastname, String firstname) throws RepositoryException;

	/**
	 * Adds the work item to user.
	 *
	 * @param userId the user id
	 * @param workId the work id
	 * @throws RepositoryException the repository exception
	 */
	void addWorkItemToUser(String userId, String workId) throws RepositoryException;
	
	/**
	 * Gets the all work items by user.
	 *
	 * @param id the id
	 * @return the all work items by user
	 * @throws RepositoryException the repository exception
	 */
	List<WorkItem> getAllWorkItemsByUser(String id) throws RepositoryException;
}
