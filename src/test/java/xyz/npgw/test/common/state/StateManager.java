package xyz.npgw.test.common.state;

import xyz.npgw.test.common.base.RunAs;

import java.time.LocalTime;

public class StateManager {

    private static final ThreadLocal<State> USER_STATE = ThreadLocal.withInitial(State::new);

    public static void setState(RunAs runAs) {
        State state = USER_STATE.get();
        LocalTime localTime = LocalTime.now().plusMinutes(14);

        switch (runAs) {
            case SUPER -> state.setSuperExpiration(localTime);
            case ADMIN -> state.setAdminExpiration(localTime);
            case USER -> state.setUserExpiration(localTime);
            default -> {
            }
        }
    }

    public static boolean isOk(RunAs runAs) {
        State state = USER_STATE.get();
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
