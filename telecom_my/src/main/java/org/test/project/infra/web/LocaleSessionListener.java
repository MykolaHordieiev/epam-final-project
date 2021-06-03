package org.test.project.infra.web;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class LocaleSessionListener implements HttpSessionListener {

    private static Logger logger = LogManager.getLogger(LocaleSessionListener.class);

    private final List<Locale> locales;
    private final Locale selectedLocale;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        logger.info("Session created");

        session.setAttribute("locales", locales);
        logger.info("Set session locales --> " + locales);

        session.setAttribute("selectedLocale", selectedLocale);
        logger.info("Set session selected locale --> " + selectedLocale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("session destroyed");
    }
}
