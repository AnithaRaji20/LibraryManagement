package library;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private ResourceBundle messages;

    public LocalizationManager(Locale locale) {
        messages = ResourceBundle.getBundle("messages", locale);
    }

    public String getMessage(String key) {
        return messages.getString(key);
    }

    public String getMessage(String key, Object... params) {
        return MessageFormat.format(getMessage(key), params);
    }
}
