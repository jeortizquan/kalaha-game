package com.lighthouse.kalah;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lighthouse.kalah.config.KalahConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(KalahConfiguration.class)
public class KalahControllerITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    public void welcomeIndex() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to Kalah API Game!")));
    }

    @Test
    public void gameCreation() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/games")).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.uri").exists());
    }

    @Test
    public void gameMove() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/games")).andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.uri").exists())
                .andDo(mvcResult -> {
                    JsonObject jsonObject = gson.fromJson(mvcResult.getResponse().getContentAsString(), JsonObject.class);

                    this.mockMvc.perform(put("http://localhost:8080/games/{gamesId}/pits/{pitId}", jsonObject.get("id").getAsString(), 1))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.id", containsString(jsonObject.get("id").getAsString())))
                            .andExpect(jsonPath("$.url").exists())
                            .andExpect(jsonPath("$.status").exists());
                });
    }
}
