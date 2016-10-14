package se.root.ordersystem.service;

import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;

import java.util.List;

/**
 * The Class WorkItemService- Work Item - an item that is assigned to a User
 * Features: - Create a Work Item - Change the status to:UNSTARTED, STARTED or
 * DONE - Remove work item -add work item to User - Download all the work item
 * based on status - Download all the work item for the Team - Download all the
 * work item for a User
 *
 * -A WorkItem can not be assigned to a user who is disabled - A User can have a
 * maximum of 5 while Work Items
 *
 * @author Root Group
 * @version 1.0.
 */
public final class WorkItemService {

    private final WorkItemRepository workItemRepository;

    public WorkItemService(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    /**
     * Change work item status.
     *
     * @param workItemId     the work item id
     * @param workItemStatus the work item status
     * @throws ServiceException the service exception
     */
    public void changeWorkItemStatus(String workItemId, WorkItemStatus workItemStatus) throws ServiceException {
        try {
            if (workItemRepository.read(workItemId).isActive()) {
                workItemRepository.changeWorkItemStatus(workItemId, workItemStatus);
            } else {
                throw new ServiceException("The workitem is not active and can not be changed");
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't change work item with id '" + workItemId + "' to status '"
                    + workItemStatus + "' in the database");
        }
    }

    /**
     * Creates the work item.
     *
     * @param workItem the work item
     * @return the work item
     * @throws ServiceException the service exception
     */
    public WorkItem createWorkItem(WorkItem workItem) throws ServiceException {
        try {
            long generatedId = workItemRepository.create(workItem);
            return WorkItem.workItemBuilder(workItem.getName()).setIssue_id(workItem.getIssueId())
                    .setStatus(workItem.getStatus()).setId(String.valueOf(generatedId)).build();
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't insert work item with id '" + workItem.getId() + "' in the database");
        }
    }

    /**
     * Update work item.
     *
     * @param workItem the work item
     * @throws ServiceException the service exception
     */
    public void updateWorkItem(WorkItem workItem) throws ServiceException {
        try {
            if (workItemRepository.read(workItem.getId()) == null) {
                throw new ServiceException("Cannot update workitem, id doesn't exist");
            }
            if (!workItem.isActive()) {
                throw new ServiceException("The workitem is not active and can not be updated");
            }
            workItemRepository.update(workItem);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update work item: " + workItem.getName());
        }
    }

    /**
     * Gets the work item.
     *
     * @param id the id
     * @return the work item
     * @throws ServiceException the service exception
     */
    public WorkItem getWorkItem(String id) throws ServiceException {
        try {
            return workItemRepository.read(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get workitem", e);
        }
    }

    /**
     * Inactivate work item.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void inactivateWorkItem(String id) throws ServiceException {
        try {
            workItemRepository.changeStatus(false, id);

        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't inactivate work item with id '" + id + "' in the database");
        }
    }

    /**
     * Activate work item.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void activateWorkItem(String id) throws ServiceException {
        try {
            workItemRepository.changeStatus(true, id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't activate work item with id '" + id + "' in the database");
        }
    }

    /**
     * Gets the all work items.
     *
     * @return the all work items
     * @throws ServiceException the service exception
     */
    public List<WorkItem> getAllWorkItems() throws ServiceException {
        try {
            return workItemRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't get all work items from the database");
        }
    }

    /**
     * Gets the work item by status.
     *
     * @param workItemStatus the work item status
     * @return the work item by status
     * @throws ServiceException the service exception
     */
    public List<WorkItem> getWorkItemByStatus(WorkItemStatus workItemStatus) throws ServiceException {
        try {
            return workItemRepository.getWorkItemByStatus(workItemStatus);
        } catch (RepositoryException e) {
            throw new ServiceException(
                    "Couldn't get all work items with status '" + workItemStatus + "' from the database");
        }
    }

    /**
     * Gets the all work items by team.
     *
     * @param id the id
     * @return the all work items by team
     * @throws ServiceException the service exception
     */
    public List<WorkItem> getAllWorkItemsByTeam(String id) throws ServiceException {
        try {
            return workItemRepository.getAllWorkItemsByTeam(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't get work items from team with id '" + id + "' from the database");
        }
    }
}
