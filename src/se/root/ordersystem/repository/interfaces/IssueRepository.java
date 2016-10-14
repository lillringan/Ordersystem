package se.root.ordersystem.repository.interfaces;

import java.util.List;

import se.root.ordersystem.model.Issue;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.exception.RepositoryException;


/**
 * The Interface IssueRepository.
 * @author Root Group
 * @version 1.0.
 */
public interface IssueRepository extends CRUDRepository<Issue> {
	
	/* (non-Javadoc)
	 * @see se.root.ordersystem.repository.interfaces.CRUDRepository#create(java.lang.Object)
	 */
	long create(Issue issue) throws RepositoryException;
	
	/* (non-Javadoc)
	 * @see se.root.ordersystem.repository.interfaces.CRUDRepository#update(java.lang.Object)
	 */
	void update(Issue issue) throws RepositoryException;
	
	/**
	 * Read.
	 *
	 * @param id the id
	 * @return the issue
	 * @throws RepositoryException the repository exception
	 */
	Issue read(String id) throws RepositoryException;
	
	/**
	 * Change status.
	 *
	 * @param isActive the is active
	 * @param id the id
	 * @throws RepositoryException the repository exception
	 */
	void changeStatus(boolean isActive, String id) throws RepositoryException;
	
	/**
	 * Gets the all issue.
	 *
	 * @return the all
	 * @throws RepositoryException the repository exception
	 */
	List<Issue> getAll() throws RepositoryException;

	/**
	 * Adds the issue to work item.
	 *
	 * @param issue the issue
	 * @param workItemId the work item id
	 * @throws RepositoryException the repository exception
	 */
	void AddIssueToWorkItem(Issue issue, String workItemId) throws RepositoryException;

	/**
	 * Gets the all work items with issue.
	 *
	 * @return the all work items with issue
	 * @throws RepositoryException the repository exception
	 */
	List<WorkItem> getAllWorkItemsWithIssue() throws RepositoryException;
}
