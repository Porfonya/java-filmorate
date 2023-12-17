package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class Friends {
    @Min(1)
    private Long userId;
    @Min(1)
    private Long friendId;
    private Boolean isFriendship;

    public Friends(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}
