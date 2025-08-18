package xyz.npgw.test.common.util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.options.ColorScheme;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class BrowserUtils {

    private static final String PREFIX_PROP_PLAYWRIGHT = "playwright.";
    private static final String PREFIX_PROP_OPTIONS = PREFIX_PROP_PLAYWRIGHT + "options.";
    private static final String PROP_OPTIONS_CREATE = PREFIX_PROP_OPTIONS + "create";
    private static final String PROP_OPTIONS_LAUNCH = PREFIX_PROP_OPTIONS + "launch";
    private static final String PROP_OPTIONS_CONTEXT = PREFIX_PROP_OPTIONS + "context";
    private static final String PROP_OPTIONS_TRACING = PREFIX_PROP_OPTIONS + "tracing";

    private static final Function<Playwright, BrowserType> browserTypeFunction;
    private static final Playwright.CreateOptions createOptions;
    private static final BrowserType.LaunchOptions launchOptions;
    private static final Browser.NewContextOptions contextOptions;
    private static final Tracing.StartOptions tracingOptions;

    static {
        browserTypeFunction = Optional.ofNullable(PropertyUtils.getValue(PREFIX_PROP_PLAYWRIGHT + "browser"))
                .map(name -> getMethodOrNull(Playwright.class, name, 0))
                .map(method -> (Function<Playwright, BrowserType>) playwright -> (BrowserType) invokeMethod(playwright, method))
                .orElseThrow(() -> new RuntimeException("Browser not found"));

        createOptions = new Playwright.CreateOptions();
        initObject(createOptions, PROP_OPTIONS_CREATE);

        launchOptions = new BrowserType.LaunchOptions();
        initObject(launchOptions, PROP_OPTIONS_LAUNCH);

        contextOptions = new Browser.NewContextOptions();
        initObject(contextOptions, PROP_OPTIONS_CONTEXT);

        tracingOptions = new Tracing.StartOptions();
        initObject(tracingOptions, PROP_OPTIONS_TRACING);
    }

    private static Method getMethodOrNull(Class<?> clazz, String methodName, int argsCount) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName) && method.getParameterCount() == argsCount)
                .findAny()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeMethod(Object instance, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return (T) method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object[] getObjectArgs(Method method, String... args) {
        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length != args.length) {
            throw new RuntimeException("The number of parameters in method {%s} does not match the number of arguments in line {%s}"
                    .formatted(method.getName(), Arrays.toString(args)));
        }

        Object[] result = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            result[i] = switch (parameterTypes[i].getTypeName()) {
                case "boolean" -> Boolean.valueOf(args[i]);
                case "int" -> Integer.valueOf(args[i]);
                case "double" -> Double.valueOf(args[i]);
                case "java.lang.String" -> args[i];
                case "java.util.Map" -> Arrays.stream(args[i].split("\\|"))
                        .map(entryStr -> entryStr.split(":", 2))
                        .collect(Collectors.toMap(entryArr -> entryArr[0], entryArr -> entryArr[1]));
                case "com.microsoft.playwright.options.ColorScheme" -> ColorScheme.valueOf(args[i]);
                case "java.nio.file.Path" -> Paths.get(args[i]);
                case "java.util.List" -> Arrays.stream(args[i].split("\\|")).toList();
                default -> throw new RuntimeException("Type {%s} not found".formatted(parameterTypes[i].getTypeName()));
            };
        }
        return result;
    }

    public static void initObject(Class<?> clazz, String paramName) {
        Optional.ofNullable(PropertyUtils.getValue(paramName))
                .map(str -> List.of(str.split(";")))
                .orElse(List.of())
                .forEach(arg -> {
                    String[] methodParams = arg.trim().split("=");
                    if (methodParams.length > 1) {
                        String[] methodArgs = methodParams[1].split(",");

                        Method method = getMethodOrNull(clazz, methodParams[0], methodArgs.length);
                        if (method != null) {
                            invokeMethod(null, method,
                                    getObjectArgs(method, methodArgs));
                        }
                    }
                });
    }

    public static <T> T initObject(T object, String paramName) {
        Optional.ofNullable(PropertyUtils.getValue(paramName))
                .map(str -> List.of(str.split(";")))
                .orElse(List.of())
                .forEach(arg -> {
                    String[] methodParams = arg.trim().split("=");
                    if (methodParams.length > 1) {
                        String[] methodArgs = methodParams[1].split(",");

                        Method method = getMethodOrNull(object.getClass(), methodParams[0], methodArgs.length);
                        if (method != null) {
                            invokeMethod(object, method,
                                    getObjectArgs(method, methodArgs));
                        }
                    }
                });

        return object;
    }

    public static Playwright createPlaywright() {
        return Playwright.create(createOptions);
    }

    public static Browser createBrowser(Playwright playwright) {
        return browserTypeFunction.apply(playwright).launch(launchOptions);
    }

    public static BrowserContext createContext(Browser browser) {
        return browser.newContext(contextOptions);
    }

    public static void startTracing(BrowserContext context) {
        context.tracing().start(tracingOptions);
    }
}
