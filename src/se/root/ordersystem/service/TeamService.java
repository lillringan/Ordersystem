package se.root.ordersystem.service;

import java.util.List;

import se.root.ordersystem.model.Team;
import se.root.ordersystem.model.User;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.repository.interfaces.TeamRepository;
import se.root.ordersystem.repository.interfaces.UserRepository;

/**
 * The Class TeamService - A grouping of User Features: - Create a team -
 * Updating a team - Disable a team - Download all the teams - Adding a User to
 * a team.
 * - maximum of 10 users in a team - A User can only be part of one team at the
 * time
 *
 * @author Root Group
 * @version 1.0.
 */
public final class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets the users from team.
     *
     * @param id the id
     * @return the users from team
     * @throws ServiceException the service exception
     */
    public List<User> getUsersFromTeam(String id) throws ServiceException {
        try {
            return teamRepository.getUsersFromTeam(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't get user from team");
        }
    }

    /**
     * Creates the team.
     *
     * @param team the team
     * @return the team
     * @throws ServiceException the service exception
     */
    public Team createTeam(Team team) throws ServiceException {
        try {
            long generatedId = teamRepository.create(team);
            return Team.teamBuilder(team.getName()).setId(String.valueOf(generatedId)).build();

        } catch (RepositoryException e) {
            throw new ServiceException("Failed to create team");
        }
    }

    /**
     * Update team.
     *
     * @param team the team
     * @throws ServiceException the service exception
     */
    public void updateTeam(Team team) throws ServiceException {
        try {
            if (teamRepository.read(team.getId()) == null) {
                throw new ServiceException("Cannot update team, id doesn't exist");
            }
            if (!team.isActive()) {
                throw new ServiceException("The team is not active and can not be updated");
            }
            teamRepository.update(team);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update team");
        }
    }

    /**
     * Inactivate team.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void inactivateTeam(String id) throws ServiceException {
        try {
            teamRepository.changeStatus(false, id);
        } catch (RepositoryException e) {
            if (e.getMessage().contains("Could not update change status for id:")) {
                throw new ServiceException(e.getMessage());
            } else {
                throw new ServiceException("Couldn't inactivate team with id '" + id + "' in the database");
            }
        }
    }

    /**
     * Activate team.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void activateTeam(String id) throws ServiceException {
        try {
            teamRepository.changeStatus(true, id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't activate team with id '" + id + "' in the database");
        }
    }

    /**
     * Get all teams.
     *
     * @return the all teams
     * @throws ServiceException the service exception
     */
    public List<Team> getAllTeams() throws ServiceException {
        try {
            return teamRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get all teams", e);
        }
    }

    /**
     * Adds the user to team if user is active
     *
     * @param userId the user id
     * @return 
     * @throws ServiceException the service exception
     */
    public long addUserToTeam(String userId) throws ServiceException {
    	  
    	  try {
    	   if (!userRepository.read(userId).isActive()) {
    	    throw new ServiceException("Could not add user to team since it's inactive");
    	   }
    	   
    	    for (Team t : teamRepository.getAll()) {
    	     
    	     if (teamRepository.getUsersFromTeam(t.getId()).size() < 10) {
    	      teamRepository.addUserToTeam(userId, t.getId());
    	      return Long.valueOf(t.getId());
    	     }
    	    }
    	    System.out.println("size "+ (teamRepository.getAll().size()+ 1));
    	     long teamId = teamRepository.create
    	         (new Team.TeamBuilder("Team " + (teamRepository.getAll().size() + 1)).build());
    	     teamRepository.addUserToTeam(userId, String.valueOf(teamId));
    	   
    	    return teamId;
    	    
    	  } catch (RepositoryException e) {
    	   throw new ServiceException("Could not add user to team", e);
    	  }
    }
}
