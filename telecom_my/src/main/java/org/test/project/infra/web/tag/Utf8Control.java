package org.test.project.infra.web.tag;

import lombok.SneakyThrows;
import org.apache.jasper.tagplugins.jstl.core.Url;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Utf8Control extends ResourceBundle.Control {

    @SneakyThrows
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
    {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        ResourceBundle resourceBundle = null;
        InputStream inputStream = null;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    connection.setUseCaches(false);
                    inputStream = connection.getInputStream();
                }
            }
        } else {
            inputStream = loader.getResourceAsStream(resourceName);
        }
        if (inputStream != null) {
            try {
                resourceBundle = new PropertyResourceBundle(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } finally {
                inputStream.close();
            }
        }
        return resourceBundle;
    }
}
