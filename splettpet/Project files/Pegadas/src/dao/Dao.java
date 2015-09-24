package dao;

import java.util.List;

public interface Dao<T>
{
	public void salvar(T obj);

	public T update(T obj);

	public void remover(T obj);
	
	public T findById(int id);
	
	public T findById(long id);
	
	public List<T> listAll();
	
}