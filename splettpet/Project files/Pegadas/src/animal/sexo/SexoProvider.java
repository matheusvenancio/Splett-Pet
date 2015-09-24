package animal.sexo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="sexoProvider")
@ViewScoped
public class SexoProvider {
	public Sexo[] getSexos()
    {
        return Sexo.values();
    }

}
