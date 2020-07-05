package net.maksym.developermanager.uitl;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthorizationUtil {

    private static String token;

    public static String getToken(MockMvc mockMvc) throws Exception {

        if (token != null) {
            return token;
        }

        synchronized (AuthorizationUtil.class) {
            if (token == null) {
                ResultActions resultActions = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"Admin\", \"password\": \"pass\" }"));

                String resultString = resultActions.andReturn().getResponse().getContentAsString();
                JacksonJsonParser jsonParser = new JacksonJsonParser();
                token = jsonParser.parseMap(resultString).get("token").toString();
            }
        }
        return token;
    }

}
