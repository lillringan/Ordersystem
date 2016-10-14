package se.root.ordersystem.repository;

import static se.root.ordersystem.helpers.DBInfo.url;

import java.sql.SQLException;
import java.util.List;

import se.root.ordersystem.helpers.ResultMapper;
import se.root.ordersystem.helpers.SQL;
import se.root.ordersystem.exception.RepositoryException;
import se.root.ordersystem.repository.interfaces.CRUDRepository;

public abstract class BaseCRUDRepository<T> implements CRUDRepository<T> {

	private final String tablename;

	public BaseCRUDRepository(String tablename) {
		this.tablename = tablename;
	}

	@Override
	public abstract long create(T t) throws RepositoryException;

	@Override
	public abstract void update(T t) throws RepositoryException;

	@Override
	public T read(String id, ResultMapper<T> resultMapper) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM " + tablename + " WHERE id = ?").parameter(id)
					.single(resultMapper);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RepositoryException("could not read data from '" + tablename + "' with id: " + id, e);
		}
	}

	@Override
	public void changeStatus(boolean isActive, String id) throws RepositoryException {
		try {
			new SQL(url).query("UPDATE " + tablename + " SET is_active= ? WHERE id = ?").parameter(isActive)
					.parameter(id).update();
		} catch (SQLException e) {
			throw new RepositoryException(
					"Could not change active status on data from '" + tablename + "' with id: " + id, e);
		}
	}

	@Override
	public List<T> getAll(ResultMapper<T> resultMapper) throws RepositoryException {
		try {
			return new SQL(url).query("SELECT * FROM " + tablename).many(resultMapper);
		} catch (SQLException e) {
			throw new RepositoryException("could not get all the data from table: " + tablename, e);
		}
	}
}
