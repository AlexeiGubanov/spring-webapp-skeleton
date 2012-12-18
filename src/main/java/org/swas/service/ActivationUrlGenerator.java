package org.swas.service;

import org.swas.domain.User;

/**
 * »нтерфейс дл€ генерации url дл€ активации.
 * Ќеобходим в св€зи с тем, что сервисный слой не может знать о контексте выполнени€ самого приложени€,
 * в частности дл€ веб-приложений необходимо знать им€ сервера и contextPath.
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public interface ActivationUrlGenerator {

    String generateUrl(User user);
}
