package edu.java.bot.service;

public interface UpdateNotifierService {
    void notify(String description, String url, Iterable<Long> chatIds);
}
