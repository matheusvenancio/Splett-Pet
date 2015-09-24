package sessaoExpirada;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FiltraAcessoPaginas implements Filter {  
	  
    public void init(FilterConfig config) throws ServletException {}  
    public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {  
          
        String msg = "";  
  
        HttpSession session = ((HttpServletRequest) request).getSession();  
  
        session.setMaxInactiveInterval(1000);  
        long ultimoAcesso = session.getLastAccessedTime();  
        long novoAcesso = java.util.Calendar.getInstance().getTimeInMillis();  
        long inatividadeMaxima = (session.getMaxInactiveInterval() * 100);  
        long ativo = novoAcesso - ultimoAcesso;  
          
        if (ativo > inatividadeMaxima) {  
            msg = "Sess‹o expirada. P‡gina com mais de 30 min sem atividade.";  
            ((HttpServletResponse) response)  
                    .sendRedirect("/Pegadas/sessaoExpirada.xhtml");  
        } else {  
            chain.doFilter(request, response);  
        }         
    }  
    public void destroy() {}  
    
}  