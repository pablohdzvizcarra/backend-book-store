package com.pablojvm.application;

import com.pablojvm.domain.User;

public interface operationsDatabase
{
    /**
     * Implementation to save a {@link User} in database.
     *
     * @param user a {@link User} with correct data
     * @return the id of user created in the database, otherwise it will return 0
     */
    int saveUser(User user);
}