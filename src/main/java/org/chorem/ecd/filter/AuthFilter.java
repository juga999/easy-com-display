package org.chorem.ecd.filter;

import com.auth0.jwt.interfaces.Payload;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.enterprise.context.ApplicationScoped;
import java.net.HttpURLConnection;
import java.util.Optional;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
@ApplicationScoped
public class AuthFilter extends AbstractFilter {

    @Override
    protected void init() {
        Spark.before(this);
    }

    @Override
    public void handle(Request req, Response resp) {
        req.attribute("user", null);
        String authHeaderVal = Optional.ofNullable(req.headers("Authorization"))
                .orElse("");
        if (authHeaderVal.startsWith("Bearer")) {
            String signedToken = authHeaderVal.split(" ")[1];
            Payload token = authService.decodeSignedToken(signedToken);
            if (token != null) {
                req.attribute("user", token.getSubject());
            } else {
                resp.header("WWW-Authenticate", "Bearer realm=\"Protected area\"");
                Spark.halt(HttpURLConnection.HTTP_UNAUTHORIZED);
            }
        }
    }

}
