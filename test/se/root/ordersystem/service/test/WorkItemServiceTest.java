package se.root.ordersystem.service.test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.exception.ServiceException;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.repository.interfaces.TeamRepository;
import se.root.ordersystem.repository.interfaces.UserRepository;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;
import se.root.ordersystem.service.WorkItemService;

@RunWith(MockitoJUnitRunner.class)

public final class WorkItemServiceTest {

	@Mock
	private WorkItemRepository workItemRepository;

	@InjectMocks
	private WorkItemService workItemService;

	private static WorkItem workItem1;
	private static WorkItem workItem2;
	private static List<WorkItem> workItems;

	@BeforeClass
	public static void setUp() {

		workItem1 = new WorkItem.WorkItemBuilder("workitem1?").setId("1").setStatus(WorkItemStatus.UNSTARTED).build();
		workItem2 = new WorkItem.WorkItemBuilder("workitem2").build();
		workItems = new ArrayList<>();
		workItems.add(workItem1);
		workItems.add(workItem2);
	}

	@Test
	public void createWorkItem() throws ServiceException, RepositoryException {

		workItemService.createWorkItem(workItem1);
		verify(workItemRepository).create(workItem1);

	}

	@Test
	public void updateWorkItem() throws RepositoryException, ServiceException {

		WorkItem updatedWorkItem = new WorkItem.WorkItemBuilder("UpdatedProblem").setId("1")
				.setStatus(WorkItemStatus.UNSTARTED).build();

		when(workItemRepository.read(workItem1.getId())).thenReturn(workItem1);
		assertEquals(updatedWorkItem.getId(), workItem1.getId());

		workItemService.updateWorkItem(updatedWorkItem);
		verify(workItemRepository).update(updatedWorkItem);

	}

	@Test
	public void getWorkItem() throws ServiceException, RepositoryException {

		String workItemId = "1";

		when(workItemRepository.read(workItemId)).thenReturn(workItem1);

		workItemService.getWorkItem(workItemId);
		verify(workItemRepository).read(workItemId);

	}

	@Test
	public void activateWorkItem() throws ServiceException, RepositoryException {

		String workItemId = "1";
		boolean isActive = true;

		workItemService.activateWorkItem(workItemId);
		verify(workItemRepository).changeStatus(isActive, workItemId);

	}

	@Test
	public void inactivateWorkItem() throws ServiceException, RepositoryException {
		String workItemId = "1";
		boolean isActive = false;

		workItemService.inactivateWorkItem(workItemId);
		verify(workItemRepository).changeStatus(isActive, workItemId);
	}

	@Test
	public void getAllWorkItems() throws ServiceException, RepositoryException {
		when(workItemRepository.getAll()).thenReturn(workItems);
		workItemService.getAllWorkItems();
	}

	@Test
	public void getWorkItemByStatus() throws RepositoryException, ServiceException {

		WorkItemStatus status = WorkItemStatus.UNSTARTED;

		// TODO Return a list of items where workItems.getStatus == status
		when(workItemRepository.getWorkItemByStatus(status)).thenReturn(workItems);

		workItemService.getWorkItemByStatus(status);
		verify(workItemRepository).getWorkItemByStatus(status);
	}

	@Test
	public void getAllWorkItemsByTeam() throws ServiceException, RepositoryException {

		String teamId = "1";

		// TODO Get workItems from teamId if list has multiple team ids
		when(workItemRepository.getAllWorkItemsByTeam(teamId)).thenReturn(workItems);

		workItemService.getAllWorkItemsByTeam(teamId);

		verify(workItemRepository).getAllWorkItemsByTeam(teamId);

	}
	
	@Test
	public void changeWorkItemStatus() throws ServiceException, RepositoryException{
		
		String workitemId = "1";
		
		WorkItemStatus workItemStatus = WorkItemStatus.STARTED;
		
		when(workItemRepository.read(workitemId)).thenReturn(workItem1);
		
		workItemRepository.changeWorkItemStatus(workitemId, workItemStatus);
		verify(workItemRepository).changeWorkItemStatus(workitemId, workItemStatus);
	}

}
