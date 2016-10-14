package se.root.ordersystem.service;

import java.util.List;

import se.root.ordersystem.model.User;
import se.root.ordersystem.model.WorkItem;

import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.repository.interfaces.TeamRepository;
import se.root.ordersystem.repository.interfaces.UserRepository;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;

/**
 * The Class UserService- user system belonging to a team features: - Create a
 * User - Updating a User - Disable a User - Download a user based on user ID /
 * number - Search for a user based on the first and / or last name and / or
 * user - Download all User as part of a certain team.
 *
 * - A User must have a username that is at least 10 characters - When a User
 * get inactivated the status of all his WorkItem will be change to un started
 *
 * @author Root Group
 * @version 1.0.
 */
public final class UserService {

    private final UserRepository userRepository;
    private final WorkItemRepository workItemRepository;

    public UserService(UserRepository userRepository, WorkItemRepository workItemRepository) {
        this.userRepository = userRepository;
        this.workItemRepository = workItemRepository;
    }

    /**
     * Creates the user.
     *
     * @param user the user
     * @return the user
     * @throws ServiceException the service exception
     */
    public User createUser(User user) throws ServiceException {
        try {
            if (user.getUsername().length() >= 10) {
                long createdId = userRepository.create(user);
                return User.userBuilder(user.getUsername(), user.getFirstname(), user.getLastname())
                        .setTeamId(user.getTeamId()).setId(String.valueOf(createdId)).build();
            } else {
                throw new ServiceException("Username too short, 10 characters required");
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to create user");
        }
    }

    /**
     * Update user.
     *
     * @param user the user
     * @throws ServiceException the service exception
     */
    public void updateUser(User user) throws ServiceException {
        try {
            if (userRepository.read(user.getId()) == null) {
                throw new ServiceException("Cannot update user, id doesn't exist");
            }
            if (!user.isActive()) {
                throw new ServiceException("The user is not active and can not be updated");
            }
            if (user.getUsername().length() < 10) {
                throw new ServiceException("Username too short, 10 characters required");
            }
            userRepository.update(user);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update user");
        }
    }

    /**
     * Gets the user by id.
     *
     * @param id the id
     * @return the user by id
     * @throws ServiceException the service exception
     */
    public User getUserById(String id) throws ServiceException {
        try {
            return userRepository.read(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Could not find user by id " + id, e);
        }
    }

    public List<User> getUsersBy(String username, String firstname, String lastname) throws ServiceException {
        try {
            return userRepository.getUsersBy(username, firstname, lastname);
        } catch (RepositoryException e) {
            throw new ServiceException("Could not find user with any of the following parameters: " + firstname, e);
        }
    }

    /**
     * Inactivate user.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void inactivateUser(String id) throws ServiceException {
        try {
            userRepository.changeStatus(false, id);
            List<WorkItem> workItems = userRepository.getAllWorkItemsByUser(id);
            for (WorkItem w : workItems) {
                workItemRepository.changeWorkItemStatus(w.getId(), WorkItemStatus.UNSTARTED);
            }
        } catch (RepositoryException e) {
            if (e.getMessage().contains("Could not update change status for id:")) {
                throw new ServiceException(e.getMessage());
            } else {
                throw new ServiceException("Couldn't inactivate user with id '" + id + "' in the database");
            }
        }
    }

    /**
     * Activate user.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void activateUser(String id) throws ServiceException {
        try {
            userRepository.changeStatus(true, id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't activate user with id '" + id + "' in the database");
        }
    }

    /**
     * Gets the all.
     *
     * @return the all
     * @throws ServiceException the service exception
     */
    public List<User> getAll() throws ServiceException {
        try {
            return userRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get all users", e);
        }
    }

    /**
     * Adds the work item to user.
     *
     * @param userId the user id
     * @param workId the work id
     * @throws ServiceException the service exception
     */
    public void addWorkItemToUser(String userId, String workId) throws ServiceException {
        try {
            if (!userRepository.read(userId).isActive()) {
                throw new ServiceException("Could not add work item to user since it's inactive");
            }
            if (userRepository.getAllWorkItemsByUser(userId).size() >= 5) {
                throw new ServiceException("Could not add work item to user since it already has 5 work items");
            }
            userRepository.addWorkItemToUser(userId, workId);
        } catch (RepositoryException e) {
            throw new ServiceException("Could not add workitem to user", e);
        }
    }

    /**
     * Gets the all work items by user.
     *
     * @return the all work items by user
     * @throws ServiceException the service exception
     */
    public List<WorkItem> getAllWorkItemsByUser(String userId) throws ServiceException {
        try {
            return userRepository.getAllWorkItemsByUser(userId);
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get workitem list");
        }
    }
}
