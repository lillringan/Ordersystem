package se.root.ordersystem.repository.interfaces;

import java.util.List;

import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.exception.RepositoryException;

/**
 * The Interface WorkItemRepository.
 * @author Root Group
 * @version 1.0.
 */
public interface WorkItemRepository extends CRUDRepository<WorkItem> {

	long create(WorkItem workItem) throws RepositoryException;

	void update(WorkItem workItem) throws RepositoryException;

	/**
	 * Read.
	 *
	 * @param id the id
	 * @return the work item
	 * @throws RepositoryException the repository exception
	 */
	WorkItem read(String id) throws RepositoryException;

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
	List<WorkItem> getAll() throws RepositoryException;

	/**
	 * Change work item status.
	 *
	 * @param id the id
	 * @param workItemStatus the work item status
	 * @throws RepositoryException the repository exception
	 */
	void changeWorkItemStatus(String id, WorkItemStatus workItemStatus) throws RepositoryException;

	/**
	 * Gets the work item by status.
	 *
	 * @param workItemStatus the work item status
	 * @return the work item by status
	 * @throws RepositoryException the repository exception
	 */
	List<WorkItem> getWorkItemByStatus(WorkItemStatus workItemStatus) throws RepositoryException;

	/**
	 * Gets the all work items by team.
	 *
	 * @param id the id
	 * @return the all work items by team
	 * @throws RepositoryException the repository exception
	 */
	List<WorkItem> getAllWorkItemsByTeam(String id) throws RepositoryException;
}
