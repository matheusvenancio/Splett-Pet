package animal.cor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="corProvider")
@ViewScoped
public class CorProvider {
	public Cor[] getCores()
    {
        return Cor.values();
    }

}