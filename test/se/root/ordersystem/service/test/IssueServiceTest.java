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
import se.root.ordersystem.model.Issue;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.repository.interfaces.IssueRepository;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;
import se.root.ordersystem.service.IssueService;

@RunWith(MockitoJUnitRunner.class)

public final class IssueServiceTest {

	@Mock
	private IssueRepository issueRepository;

	@Mock
	private WorkItemRepository workItemRepository;

	@InjectMocks
	private IssueService issueService;

	private static Issue issue1;
	private static Issue issue2;
	private static List<Issue> issues;

	private static WorkItem workItem1;

	@BeforeClass
	public static void setUp() {
		issue1 = new Issue.IssueBuilder("issuenr1").setId("1").build();
		issue2 = new Issue.IssueBuilder("issuenr1").setId("2").build();

		issues = new ArrayList<>();
		issues.add(issue1);
		issues.add(issue2);

		workItem1 = new WorkItem.WorkItemBuilder("problem1").setId("1").setStatus(WorkItemStatus.DONE).build();
	}

	@Test
	public void addIssueToWorkItem() throws RepositoryException, ServiceException {

		Issue issue = new Issue.IssueBuilder("problem").setId("5").build();
		String workItemId = "2";

		when(workItemRepository.read(workItemId)).thenReturn(workItem1);
		when(issueRepository.create(issue)).thenReturn(5L);
		long generatedId = issueRepository.create(issue);

		assertEquals(issue.getId(), String.valueOf(generatedId));

		issueService.addIssueToWorkItem(issue, workItemId);

		verify(issueRepository).AddIssueToWorkItem(issue, workItemId);
		verify(workItemRepository).changeWorkItemStatus(workItemId, WorkItemStatus.UNSTARTED);

	}

	@Test
	public void updateIssue() throws ServiceException, RepositoryException {

		Issue updatedIssue = new Issue.IssueBuilder("updatedIssue").setId("1").build();

		when(issueRepository.read(issue1.getId())).thenReturn(issue1);
		assertEquals(updatedIssue.getId(), issue1.getId());

		issueService.updateIssue(updatedIssue);
		verify(issueRepository).update(updatedIssue);
	}

	@Test
	public void getAllWorkItemsWithIssue() throws ServiceException, RepositoryException {

		issueService.getAllWorkItemsWithIssue();
		verify(issueRepository).getAllWorkItemsWithIssue();

	}

}
