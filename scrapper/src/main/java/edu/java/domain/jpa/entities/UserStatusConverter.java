package edu.java.domain.jpa.entities;

import edu.java.domain.mappers.UserStatusMapper;
import edu.java.models.User;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserStatusConverter implements AttributeConverter<User.Status, String> {
    @Override
    public String convertToDatabaseColumn(User.Status status) {
        return UserStatusMapper.userStatusToString(status);
    }

    @Override
    public User.Status convertToEntityAttribute(String status) {
        return UserStatusMapper.userStatusFromString(status);
    }
}
