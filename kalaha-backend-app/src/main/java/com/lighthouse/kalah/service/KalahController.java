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

    public static final Logger LOGGER = LoggerFactory.getLogger(KalahController.class);

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
            LOGGER.info("Game created id: {}", gameId);

            responseEntity = getSuccessResponseEntity(gameId, location);

        } catch (Exception ex) {
            responseEntity = getErrorResponseEntity(ex, "Failed to create game");
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
            LOGGER.info("Moving in gameid: {} with pitId: {}", gameId, pitId);

            responseEntity = getSuccessResponseEntity(gameId, location, board);

        } catch (Exception ex) {
            responseEntity = getErrorResponseEntity(ex, "Failed to move");
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
            LOGGER.info("Getting status of gameid: {}", gameId);

            responseEntity = getSuccessResponseEntity(gameId, location, board);

        } catch (Exception ex) {
            responseEntity = getErrorResponseEntity(ex, "Failed to move");
        }
        return responseEntity;
    }

    private ResponseEntity<Object> getErrorResponseEntity(Exception ex, String errorMessage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
        jsonObject.addProperty("status", "400");
        jsonObject.addProperty("error", errorMessage);
        jsonObject.addProperty("message", ex.getMessage());

        LOGGER.error("Response: {}", gson.toJson(jsonObject));
        return ResponseEntity
                .status(400)
                .body(gson.toJson(jsonObject));
    }

    private ResponseEntity<Object> getSuccessResponseEntity(Integer gameId, URI location, Kalahah board) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", String.valueOf(gameId));
        jsonObject.addProperty("url", location.toString());
        jsonObject.addProperty("turn", board.getTurn().toString());
        jsonObject.addProperty("kstatus", board.kalahahStatus().toString());
        JsonObject jsonStatus = new JsonObject();
        for (int sId = 1; sId <= 14; sId++) {
            jsonStatus.addProperty(String.valueOf(sId), board.getStatus().get(sId).toString());
        }
        jsonObject.add("status", jsonStatus);

        LOGGER.info("Response: {}", gson.toJson(jsonObject));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(jsonObject));
    }
    
    private ResponseEntity<Object> getSuccessResponseEntity(int gameId, URI location) {
        ResponseEntity<Object> responseEntity;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", String.valueOf(gameId));
        jsonObject.addProperty("uri", location.toString());
        responseEntity = ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(jsonObject));
        return responseEntity;
    }
}
