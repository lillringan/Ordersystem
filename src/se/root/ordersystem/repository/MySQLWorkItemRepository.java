package se.root.ordersystem.repository;

import se.root.ordersystem.helpers.SQL;
import se.root.ordersystem.model.WorkItem;
import se.root.ordersystem.model.WorkItemStatus;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.repository.interfaces.WorkItemRepository;

import java.sql.SQLException;
import java.util.List;

import static se.root.ordersystem.helpers.DBInfo.url;
import static se.root.ordersystem.helpers.Mapper.WORK_ITEM_MAPPER;

public final class MySQLWorkItemRepository extends BaseCRUDRepository<WorkItem> implements WorkItemRepository {

	public MySQLWorkItemRepository() {
		super("workitem");
	}

	public long create(WorkItem workItem) throws RepositoryException {
		try {
			return new SQL(url).query("INSERT INTO workitem (name) VALUES (?)").parameter(workItem.getName()).insert();
		} catch (SQLException e) {
			throw new RepositoryException("");
		}
	}

	public void update(WorkItem workItem) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE workitem SET (status) VALUES (?) WHERE id = ?").parameter(workItem.getStatus())
					.parameter(workItem.getId()).update();
		} catch (SQLException e) {
			throw new RepositoryException("");
		}
	}

	@Override
	public WorkItem read(String id) throws RepositoryException {
		return super.read(id, WORK_ITEM_MAPPER);
	}

	@Override
	public void changeStatus(boolean isActive, String id) throws RepositoryException {
		super.changeStatus(isActive, id);
	}

	@Override
	public List<WorkItem> getAll() throws RepositoryException {
		return super.getAll(WORK_ITEM_MAPPER);
	}

	@Override
	public void changeWorkItemStatus(String id, WorkItemStatus workItemStatus) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE workitem SET status = ? WHERE id = ?").parameter(workItemStatus.toString())
					.parameter(id).insert();
		} catch (SQLException e) {
			throw new RepositoryException("Could not update workitem status", e);
		}
	}

	@Override
	public List<WorkItem> getWorkItemByStatus(WorkItemStatus workItemStatus) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM workitem WHERE status = ?").parameter(workItemStatus.toString())
					.many(WORK_ITEM_MAPPER);
		} catch (SQLException e) {
			throw new RepositoryException("Could not get work item by status", e);
		}
	}

	@Override
	public List<WorkItem> getAllWorkItemsByTeam(String id) throws RepositoryException {
		try {
			return new SQL(url)
					.query("SELECT * FROM workitem LEFT JOIN user ON user.id = workitem.user_id WHERE user.team_id = ?")
					.parameter(id).many(WORK_ITEM_MAPPER);
		} catch (SQLException e) {
			throw new RepositoryException("Could not get work item by status", e);
		}
	}
}
