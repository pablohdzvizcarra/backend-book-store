package com.pablojvm.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablojvm.application.ActionsPersistenceService;
import com.pablojvm.application.UserPersistenceService;
import com.pablojvm.application.ValidationService;
import com.pablojvm.domain.DataPostUser;
import com.pablojvm.domain.User;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.javalin.http.Context;

public class UserService {
    private final ValidationService validationService;
    private final ActionsPersistenceService userPersistenceService;
    private static final Logger LOGGER =
            Logger.getLogger(UserPersistenceService.class.getName());

    public UserService(
            ValidationService service,
            ActionsPersistenceService userPersistenceService
    ) {
        this.validationService = service;
        this.userPersistenceService = userPersistenceService;
    }

    public void createUser(Context context) throws JsonProcessingException {

        String body = context.body();
        ObjectMapper objectMapper = new ObjectMapper();
        DataPostUser data =
                objectMapper.readValue(body, new TypeReference<>() {
                });

        List<String> errorsList =
                this.validationService.validateDataCreateUser(data);

        User saveUser = this.userPersistenceService.saveUser(data);

        if (errorsList.size() != 0) {
            this.responseCreateUserWithError(context, errorsList);

            LOGGER.log(
                    Level.INFO,
                    "an attempt was made to create a user with the following" +
                            "invalid data: " + errorsList
            );
        } else if (saveUser == null) {
            LOGGER.log(
                    Level.INFO,
                    "failed to save the user in the database"
            );
            this.responseWithInvalidEmail(context);
        } else {
            LOGGER.log(
                    Level.INFO,
                    "the following user was successfully saved in the database" + saveUser
            );
            context.status(201);
            context.json(saveUser);
        }
    }

    private void responseWithInvalidEmail(Context context) {
        context.status(400);
        context.result("The email is duplicated, please add another email.");
    }

    private void responseCreateUserWithError(
            Context context,
            List<String> errorsList
    ) {
        Collections.reverse(errorsList);
        context.status(400);
        context.json(errorsList);
    }
}