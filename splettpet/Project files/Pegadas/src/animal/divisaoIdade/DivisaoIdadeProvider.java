package animal.divisaoIdade;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="divisaoIdadeProvider")
@ViewScoped
public class DivisaoIdadeProvider {
	public DivisaoIdade[] getDiviIdade()
    {
        return DivisaoIdade.values();
    }

}
