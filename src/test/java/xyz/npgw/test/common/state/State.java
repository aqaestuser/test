package xyz.npgw.test.common.state;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
class State {
    private LocalTime superExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime adminExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime userExpiration = LocalTime.now().minusMinutes(1);
}
