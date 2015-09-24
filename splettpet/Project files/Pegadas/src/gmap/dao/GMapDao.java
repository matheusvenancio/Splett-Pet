package gmap.dao;

import gmap.GMap;
import dao.Dao;

public interface GMapDao extends Dao<GMap> {
	public GMap buscarGmByEnd(int id);
}
