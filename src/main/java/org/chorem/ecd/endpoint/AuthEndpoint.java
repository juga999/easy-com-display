package org.chorem.ecd.endpoint;

import spark.Request;
import spark.Response;
import spark.Spark;

import java.net.HttpURLConnection;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class AuthEndpoint extends Endpoint {

    @Override
    public void init() {
        Spark.post(ecdApiPath("/login"), this::login);
    }

    public Object login(Request req, Response resp) throws Exception {
        String user = req.queryParams("user");
        String password = req.queryParams("password");
        if (user == null || password == null) {
            Spark.halt(HttpURLConnection.HTTP_BAD_REQUEST);
        }
        String signedToken = authService.getSignedToken(user, password);
        resp.type("text/plain");
        return signedToken;
    }
}
