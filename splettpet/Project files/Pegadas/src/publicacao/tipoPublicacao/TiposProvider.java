package publicacao.tipoPublicacao;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="tiposPubliProvider")
@ViewScoped
public class TiposProvider {

	public TipoPublicacao[] getTipos()
    {
        return TipoPublicacao.values();
    }

}
