package xyz.npgw.test.common.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

public class StateManager {

    private static final ThreadLocal<State> userState = ThreadLocal.withInitial(State::new);

    public static void setState(RunAs runAs) {
        State state = userState.get();
        LocalTime localTime = LocalTime.now().plusMinutes(2); // 14);

        switch (runAs) {
            case SUPER -> state.setSuperExpiration(localTime);
            case ADMIN -> state.setAdminExpiration(localTime);
            case USER -> state.setUserExpiration(localTime);
            default -> {
            }
        }
    }

    public static boolean isOk(RunAs runAs) {
        State state = userState.get();
        LocalTime now = LocalTime.now();

        switch (runAs) {
            case SUPER -> {
                return now.isBefore(state.getSuperExpiration());
            }
            case ADMIN -> {
                return now.isBefore(state.getAdminExpiration());
            }
            case USER -> {
                return now.isBefore(state.getUserExpiration());
            }
            default -> {
                return true;
            }
        }
    }
}

@Setter
@Getter
class State {
    private LocalTime superExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime adminExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime userExpiration = LocalTime.now().minusMinutes(1);
}
