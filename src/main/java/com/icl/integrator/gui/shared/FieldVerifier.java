package com.icl.integrator.gui.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> packing because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

    public static final String IP_REGEXP =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static void validateName(String name) {
        if (name == null) {
            throw new GuiException("Значение не может быть null");
        }
        if (name.length() == 0) {
            throw new GuiException("Значение не может быть пустым");
        }
    }

    public static void validateIP(String string) {
        if (string == null) {
            throw new GuiException("Значение не может быть null");
        }
        if (!string.matches(IP_REGEXP)) {
            throw new GuiException("Значение не является валидным IP - адресом");
        }
    }

    public static int parseNumber(String fieldText, int minimum,
                                  int maximum) throws GuiException {
        try {
            int value = Integer.parseInt(fieldText);
            if (value < minimum || value > maximum) {
                throw new GuiException(
                        "Значение должно быть в интервале [" + minimum + ":" + maximum + "]");

            }
            return value;
        } catch (NumberFormatException ex) {
            throw new GuiException("Введи числовое значение. '"+fieldText+"' не канает", ex);
        }
    }

    public static int parseNumber(String fieldText, int minimum) throws GuiException {
        return parseNumber(fieldText, minimum, Integer.MAX_VALUE);
    }
}
