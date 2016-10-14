package se.root.ordersystem.service;

import java.util.List;

import se.root.ordersystem.model.Issue;
import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.repository.interfaces.IssueRepository;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;

/**
 * The Class IssueService- Create, retrieve, update an issue.
 *
 * @author Root Group
 * @version 1.0.
 *
 * issue - a remark that can be given a work item when it is not
 * accepted. Can only be added to a work item that has Status DONE, and
 * thus changes the status to UNSTARTED. Retrieve all work item which
 * has an issue.
 */

public final class IssueService {

    private final IssueRepository issueRepository;
    private final WorkItemRepository workItemRepository;

    public IssueService(IssueRepository issueRepository, WorkItemRepository workItemRepository) {
        this.issueRepository = issueRepository;
        this.workItemRepository = workItemRepository;
    }

    /**
     * Saves the Issue object in the database and adds it to an existing work
     * item.
     *
     * @param issue      The issue object that you want to store in the database.
     * @param workItemId The id of the existing work item that you want to add the issue to.
     * @return An issue object with the same values as the created row in the
     * database including its auto-generated id.
     * @throws ServiceException
     */
    public Issue addIssueToWorkItem(Issue issue, String workItemId) throws ServiceException {
        try {
            if (workItemRepository.read(workItemId).getStatus() == WorkItemStatus.DONE) {
                long generatedId = issueRepository.create(issue);
                Issue createdIssue = Issue.issueBuilder(issue.getTitle()).setDescription(issue.getDescription())
                        .setId(String.valueOf(generatedId)).build();
                issueRepository.AddIssueToWorkItem(createdIssue, workItemId);
                workItemRepository.changeWorkItemStatus(workItemId, WorkItemStatus.UNSTARTED);
                return createdIssue;
            } else {
                throw new ServiceException("Could not add issue to workitem, workitem status not DONE");
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Error when reading workitem and getting status", e);
        }
    }

    /**
     * Updates an existing issue in the database.
     *
     * @param issue A new issue object with the new values you want to change in the database.
     * @throws ServiceException
     */
    public void updateIssue(Issue issue) throws ServiceException {
        try {
            if (issueRepository.read(issue.getId()) == null) {
                throw new ServiceException("Cannot update issue, id doesn't exist");
            }
            if (!issue.getIsActive()) {
                throw new ServiceException("The issue is not active and can not be updated");
            }
            issueRepository.update(issue);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't update issue with title: " + issue.getTitle());
        }
    }

    /**
     * Gets all the work items with an issue from the database.
     *
     * @return A list of work item objects that represents all the stored work
     * items with an issue.
     * @throws ServiceException
     */
    public List<WorkItem> getAllWorkItemsWithIssue() throws ServiceException {
        try {
            return issueRepository.getAllWorkItemsWithIssue();
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get workitems", e);
        }
    }
}