package animal.porte;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="porteProvider")
@ViewScoped
public class PorteProvider {
	public Porte[] getPortes()
    {
        return Porte.values();
    }

}
