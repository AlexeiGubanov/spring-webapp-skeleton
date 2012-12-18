package org.swas.service;

import org.swas.domain.User;

/**
 * ��������� ��� ��������� url ��� ���������.
 * ��������� � ����� � ���, ��� ��������� ���� �� ����� ����� � ��������� ���������� ������ ����������,
 * � ��������� ��� ���-���������� ���������� ����� ��� ������� � contextPath.
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public interface ActivationUrlGenerator {

    String generateUrl(User user);
}
