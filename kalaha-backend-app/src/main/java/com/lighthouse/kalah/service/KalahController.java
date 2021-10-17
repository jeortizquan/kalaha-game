package com.lighthouse.kalah.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lighthouse.kalah.game.Game;
import com.lighthouse.kalah.game.Kalahah;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@CrossOrigin
@RestController
public class KalahController {

    public static final Logger logger = LoggerFactory.getLogger(KalahController.class);

    @Autowired
    private Game service;

    @Autowired
    private Gson gson;

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String index() {
        return "Welcome to Kalah API Game!";
    }

    @PostMapping(path = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> create() {
        ResponseEntity<Object> responseEntity;

        try {
            int gameId = service.create();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(gameId)
                    .toUri();
            logger.info("Game created id: {}", gameId);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", String.valueOf(gameId));
            jsonObject.addProperty("uri", location.toString());
            responseEntity = ResponseEntity
                    .created(location)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(gson.toJson(jsonObject));

        } catch (Exception ex) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", "500");
            jsonObject.addProperty("error", "Failed to create game");
            jsonObject.addProperty("message", ex.getMessage());
            responseEntity = ResponseEntity
                    .status(500)
                    .body(gson.toJson(jsonObject));
            logger.error("Response: {}", gson.toJson(jsonObject));
        }
        return responseEntity;
    }

    @PutMapping(path = "/games/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> move(@PathVariable(value = "gameId") Integer gameId,
                                       @PathVariable(value = "pitId") Integer pitId) {
        ResponseEntity<Object> responseEntity;

        try {
            Kalahah board = service.move(gameId, pitId);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/games/{gameId}")
                    .buildAndExpand(gameId)
                    .toUri();
            logger.info("Moving in gameid: {} with pitId: {}", gameId, pitId);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", String.valueOf(gameId));
            jsonObject.addProperty("url", location.toString());
            jsonObject.addProperty("turn", board.getTurn().toString());
            JsonObject jsonStatus = new JsonObject();
            for (int sId = 1; sId <= 14; sId++) {
                jsonStatus.addProperty(String.valueOf(sId), board.getStatus().get(sId).toString());
            }
            jsonObject.add("status", jsonStatus);
            responseEntity = ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(gson.toJson(jsonObject));
            logger.info("Response: {}", gson.toJson(jsonObject));

        } catch (Exception ex) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", "500");
            jsonObject.addProperty("error", "Failed to move");
            jsonObject.addProperty("message", ex.getMessage());
            responseEntity = ResponseEntity
                    .status(500)
                    .body(gson.toJson(jsonObject));
            logger.error("Response: {}", gson.toJson(jsonObject));
        }
        return responseEntity;
    }

    @GetMapping(path = "/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> status(@PathVariable(value = "gameId") Integer gameId) {
        ResponseEntity<Object> responseEntity;

        try {
            Kalahah board = service.getGame(gameId);
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/games/{gameId}")
                    .buildAndExpand(gameId)
                    .toUri();
            logger.info("Getting status of gameid: {}", gameId);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", String.valueOf(gameId));
            jsonObject.addProperty("url", location.toString());
            jsonObject.addProperty("turn", board.getTurn().toString());
            JsonObject jsonStatus = new JsonObject();
            for (int sId = 1; sId <= 14; sId++) {
                jsonStatus.addProperty(String.valueOf(sId), board.getStatus().get(sId).toString());
            }
            jsonObject.add("status", jsonStatus);
            responseEntity = ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(gson.toJson(jsonObject));
            logger.info("Response: {}", gson.toJson(jsonObject));

        } catch (Exception ex) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", "500");
            jsonObject.addProperty("error", "Failed to move");
            jsonObject.addProperty("message", ex.getMessage());
            responseEntity = ResponseEntity
                    .status(500)
                    .body(gson.toJson(jsonObject));
            logger.error("Response: {}", gson.toJson(jsonObject));
        }
        return responseEntity;
    }
}
