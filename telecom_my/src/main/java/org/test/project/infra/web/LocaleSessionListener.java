package org.test.project.infra.web;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class LocaleSessionListener implements HttpSessionListener {

    private final List<Locale> locales;
    private final Locale selectedLocale;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        session.setAttribute("locales", locales);
        session.setAttribute("selectedLocale", selectedLocale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
