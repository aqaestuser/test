package xyz.npgw.test.common.base;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalTime;

@Log4j2
public class StateManager {

    private static final ThreadLocal<State> userState = ThreadLocal.withInitial(State::new);

    public static State getState() {
        return userState.get();
    }

    public static void setState(RunAs runAs) {
        State state = userState.get();
        LocalTime  localTime = LocalTime.now().plusMinutes(14);

        log.info("set {} state - good till {}", runAs, localTime);
        switch (runAs) {
            case SUPER -> state.setSuperExpiration(localTime);
            case ADMIN -> state.setAdminExpiration(localTime);
            case USER -> state.setUserExpiration(localTime);
            default -> {}
        }
    }

    public static boolean isOk(RunAs runAs) {
        State state = userState.get();
        LocalTime now = LocalTime.now();

        switch (runAs) {
            case SUPER -> {
                log.info("check {} state - good till {}", runAs, state.getSuperExpiration());
                return now.isBefore(state.getSuperExpiration());
            }
            case ADMIN -> {
                log.info("check {} state - good till {}", runAs, state.getSuperExpiration());
                return now.isBefore(state.getAdminExpiration());
            }
            case USER -> {
                log.info("check {} state - good till {}", runAs, state.getSuperExpiration());
                return now.isBefore(state.getUserExpiration());
            }
            default -> {
                return true;
            }
        }
    }

    public static void clearState() {
        userState.remove();
    }
}

@Setter
@Getter
class State {
    private LocalTime superExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime adminExpiration = LocalTime.now().minusMinutes(1);
    private LocalTime userExpiration = LocalTime.now().minusMinutes(1);
}
