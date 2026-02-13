package br.com.felz.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.felz.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();
        System.out.println("Path");
        System.out.println(servletPath);

        if(servletPath.startsWith("/tasks")) {
            //Pegar autenticação e senha
            var authorization = request.getHeader("Authorization"); //Busca authorization

            var authEncoded = authorization.substring("Basic".length()).trim(); //Remove a palavra Basic

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded); //Decode com base64

            var authString = new String(authDecoded); //Transforma o array de bytes em string

            String[] credentials = authString.split(":"); //Transforma a string em um array username, password
            var username = credentials[0];
            var password = credentials[1];

            System.out.println("Authorization");
            System.out.println(username);
            System.out.println(password);


            //Validar usuário
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                //Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPass());
                if(passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}